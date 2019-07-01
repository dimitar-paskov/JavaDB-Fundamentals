import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Engine implements Runnable {

    private Connection connection;
    private BufferedReader reader;

    public Engine(Connection connection) {
        this.connection = connection;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() {


        this.getVillainsNames();
        this.getMinionNames();
        this.addMinions();
        this.changeTownNamesCasing();
        this.printAllMinionsNames();
        this.increaseMinionsAge();
        this.increaseAgeStoredProcedure();
        this.removeVillain();


    }




    private void getVillainsNames() {
        String query = "SELECT v.name, count(mv.minion_id) AS 'count'\n" +
                "FROM villains AS v\n" +
                "JOIN minions_villains mv\n" +
                "ON v.id = mv.villain_id\n" +
                "GROUP BY v.name\n" +
                "HAVING `count` > ?\n" +
                "ORDER BY `count` DESC;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, 15);

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                System.out.printf("%s %d",
                        result.getString("name"),
                        result.getInt("count")).println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void getMinionNames() {


        int id = Integer.MIN_VALUE;
        try {
            id = Integer.parseInt(this.reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String queryOne = "SELECT name FROM villains\n" +
                "WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(queryOne);
            preparedStatement.setInt(1, id);

            ResultSet result = preparedStatement.executeQuery();

            if (!result.isBeforeFirst()) {
                System.out.printf("No villain with ID %d exists in the database.%n", id);
            } else {
                while (result.next()) {
                    System.out.printf("Villain: %s",
                            result.getString("name")).println();

                }

                String queryTwo = "SELECT `name`, `age`\n" +
                        "FROM minions m\n" +
                        "JOIN minions_villains mv\n" +
                        "ON m.id = mv.minion_id\n" +
                        "WHERE mv.villain_id = ?;";

                PreparedStatement preparedStatementTwo = connection.prepareStatement(queryTwo);
                preparedStatementTwo.setInt(1, id);

                ResultSet resultMinions = preparedStatementTwo.executeQuery();

                int i = 1;
                while (resultMinions.next()) {
                    System.out.printf("%d. %s %d%n",
                            i++,
                            resultMinions.getString("name"),
                            resultMinions.getInt("age"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void addMinions() {

        StringBuilder sb = new StringBuilder();

        try {
            connection.setAutoCommit(false);
            System.out.print("Minion: ");
            String input = this.reader.readLine();
            String[] tokens = input.split("\\s+");
            String minionName = tokens[0];
            int age = Integer.parseInt(tokens[1]);
            String town = tokens[2];

            System.out.print("Villain: ");
            input = this.reader.readLine();
            String villainName = input.trim();

            String townQuery = "SELECT * FROM towns WHERE name=?;";
            String villainQuery = "SELECT * FROM villains WHERE name = ?;";
            String minionQuery = "SELECT * FROM minions WHERE name = ?;";


            try {

                //town
                PreparedStatement townPreparedStatement = connection.prepareStatement(townQuery);
                townPreparedStatement.setString(1, town);

                ResultSet townResult = townPreparedStatement.executeQuery();

                if (!townResult.isBeforeFirst()) {

                    String townInsertQuery = "INSERT INTO`towns`(`name`) VALUES(?);";
                    PreparedStatement townInsertPreparedStatement = connection.prepareStatement(townInsertQuery);
                    townInsertPreparedStatement.setString(1, town);
                    townInsertPreparedStatement.executeUpdate();

                    sb.append(String.format("Town %s was added to the database.%n", town));

                }

                townResult = townPreparedStatement.executeQuery();
                townResult.first();

                int townId = townResult.getInt("id");


                //villain
                PreparedStatement villainPreparedStatement = connection.prepareStatement(villainQuery);
                villainPreparedStatement.setString(1, villainName);
                ResultSet villainResult = villainPreparedStatement.executeQuery();

                if (!villainResult.isBeforeFirst()) {

                    String villainInsertQuery = "INSERT INTO`villains`(`name`, `evilness_factor`) VALUES(?, 'evil');";
                    PreparedStatement villainInsertPreparedStatement = connection.prepareStatement(villainInsertQuery);
                    villainInsertPreparedStatement.setString(1, villainName);
                    villainInsertPreparedStatement.executeUpdate();

                    sb.append(String.format("Villain %s was added to the database.%n", villainName));

                }

                villainResult = villainPreparedStatement.executeQuery();
                villainResult.first();
                int villainId = villainResult.getInt("id");


                // minions
                String minionInsertQuery = "INSERT INTO minions (`name`, `age`, `town_id`) VALUES(?, ?, ?);";
                PreparedStatement minionInsertPreparedStatement = connection.prepareStatement(minionInsertQuery);
                minionInsertPreparedStatement.setString(1, minionName);
                minionInsertPreparedStatement.setInt(2, age);
                minionInsertPreparedStatement.setInt(3, townId);
                minionInsertPreparedStatement.executeUpdate();

                PreparedStatement minionPreparedStatement = connection.prepareStatement(minionQuery);
                minionPreparedStatement.setString(1, minionName);
                ResultSet minionsResult = minionPreparedStatement.executeQuery();
                minionsResult.first();
                int minionId = minionsResult.getInt("id");


                String minionToVillainsInsertQuery = "INSERT INTO minions_villains (`minion_id`, `villain_id`) VALUES(?,?);";
                PreparedStatement minionToVillainsInsertPreparedStatement = connection.prepareStatement(minionToVillainsInsertQuery);
                minionToVillainsInsertPreparedStatement.setInt(1, minionId);
                minionToVillainsInsertPreparedStatement.setInt(2, villainId);
                minionToVillainsInsertPreparedStatement.executeUpdate();

                sb.append(String.format("Successfully added %s to be minion of %s.", minionName, villainName));
                connection.commit();

            } catch (Exception e) {
                connection.rollback();

            }

        } catch (SQLException | IOException e) {
            sb = new StringBuilder();
            e.printStackTrace();
        }

        System.out.println(sb.toString());


    }



    private void changeTownNamesCasing() {

        StringBuilder sb = new StringBuilder();

        String country = "";

        try {
            country = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String queryCount = "SELECT count(*) AS 'count'  FROM towns WHERE country = ?;";
        String queryCities = "SELECT name FROM towns WHERE country = ?;";
        String updateQuery = "UPDATE towns SET `name` = UPPER( `name` ) WHERE country = ?;";

        try {
            PreparedStatement getCountOfCities = connection.prepareStatement(queryCount);
            getCountOfCities.setString(1, country);
            ResultSet count = getCountOfCities.executeQuery();
            count.first();
            int affectedCities = count.getInt("count");


            if (affectedCities > 0) {
                sb.append("3 town names were affected. ");
                sb.append(System.lineSeparator());

                PreparedStatement updateCities = connection.prepareStatement(updateQuery);
                updateCities.setString(1, country);
                updateCities.executeUpdate();

                PreparedStatement getCities = connection.prepareStatement(queryCities);
                getCities.setString(1, country);
                ResultSet cities = getCities.executeQuery();

                sb.append("[");
                while (cities.next()) {
                    sb.append(cities.getString("name"));
                    sb.append(", ");
                }

                sb.setLength(sb.length() - 2);
                sb.append("]");


            } else {
                sb.append("No town names were affected.");
            }

            System.out.println(sb.toString());


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }



    private void printAllMinionsNames() {


        String query = "SELECT * FROM minions;";
        try {
            PreparedStatement minionsStatement = connection.prepareStatement(query);
            ResultSet result = minionsStatement.executeQuery();

            int i = 1;
            while (true) {

                result.absolute(i);
                int idFirst = result.getInt("id");
                System.out.println(result.getString("name"));
                result.absolute(-i);
                int idSecond = result.getInt("id");

                if (idSecond <= idFirst) {
                    break;
                }else {
                    System.out.println(result.getString("name"));
                }
                i++;

            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }



    private void increaseMinionsAge() {

        String input = "";
        try {
            input = this.reader.readLine().replaceAll(" ", ", ");
        } catch (IOException e) {
            e.printStackTrace();
        }


        String updateQuery = String.format("Update minions\n" +
                "SET `name` = LOWER(`name`), `age` = `age`+1\n" +
                " WHERE id IN (%s);", input);
        String selectQuery = "SELECT `name`, `age` FROM minions;";

        try {
            PreparedStatement updatePS = connection.prepareStatement(updateQuery);
            updatePS.executeUpdate();

            PreparedStatement selectPS = connection.prepareStatement(selectQuery);
            ResultSet result = selectPS.executeQuery();

            while (result.next()){
                System.out.printf("%s %d%n", result.getString("name"), result.getInt("age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }



    private void increaseAgeStoredProcedure() {

        int id = Integer.MIN_VALUE;
        try {
            id = Integer.parseInt(this.reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            CallableStatement stmt=connection.prepareCall("{call usp_get_older(?)}");
            stmt.setInt(1,id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String selectQuery = "SELECT `name`, `age` FROM minions WHERE id=?;";

        try {
            PreparedStatement selectPS = connection.prepareStatement(selectQuery);
            selectPS.setInt(1,id);
            ResultSet result = selectPS.executeQuery();
            result.first();
            System.out.printf("%s %d",
                    result.getString("name"),
                    result.getInt("age"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void removeVillain() {

        int id = Integer.MIN_VALUE;
        try {
            id = Integer.parseInt(this.reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        String selectQuery = "SELECT * FROM villains WHERE id = ?;";
        try {
            connection.setAutoCommit(false);
            PreparedStatement selectPS = connection.prepareStatement(selectQuery);
            selectPS.setInt(1,id);
            ResultSet result = selectPS.executeQuery();

            if (!result.isBeforeFirst()) {
                sb.append("No such villain was found");
                connection.rollback();
            }else{

                result.first();
                String villainName = result.getString("name");

                String deleteQueryFromMV = "DELETE FROM minions_villains WHERE villain_id = ?;";
                String selectCountFromMV = "SELECT count(*) AS count FROM minions_villains WHERE villain_id =?;";
                PreparedStatement selectCount = connection.prepareStatement(selectCountFromMV);
                selectCount.setInt(1,id);
                ResultSet countOfMinionsToBeReleased = selectCount.executeQuery();
                countOfMinionsToBeReleased.first();
                int count = countOfMinionsToBeReleased.getInt("count");

                sb.append(String.format("%d minions released", count));

                PreparedStatement deleteFromMV = connection.prepareStatement(deleteQueryFromMV);
                deleteFromMV.setInt(1,id);
                deleteFromMV.executeUpdate();



                String deleteQueryFromVillains = "DELETE FROM villains WHERE id = ?;";
                PreparedStatement deleteFromVillains = connection.prepareStatement(deleteQueryFromVillains);
                deleteFromVillains.setInt(1,id);
                deleteFromVillains.executeUpdate();

                connection.commit();

                sb.insert(0,String.format("%s was deleted%n", villainName) );

            }




        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print(sb.toString());


    }

}

