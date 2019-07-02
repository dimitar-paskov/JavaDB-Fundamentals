package softuni.usersystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import softuni.usersystem.entities.*;
import softuni.usersystem.services.AlbumService;
import softuni.usersystem.services.PictureService;
import softuni.usersystem.services.UserService;


import java.time.LocalDateTime;

@Controller
public class AppController implements CommandLineRunner {

    private final UserService userService;
    private final AlbumService albumService;
    private final PictureService pictureService;

    @Autowired
    public AppController(final UserService userService, AlbumService albumService, PictureService pictureService) {
        this.userService = userService;
        this.albumService = albumService;
        this.pictureService = pictureService;
    }

    @Override
    public void run(final String... args) {

        try {

            Country bulgaria = new Country("Bulgaria");
            Town sofia = new Town("Sofia");
            sofia.setCountry(bulgaria);

            User user = new User();

            user.setUsername("user");
            user.setPassword("passwor#Dd123");
            user.setAge(12);
            user.setEmail("user_123@abv.bg");
            user.setRegisteredOn(LocalDateTime.now());
            user.setLastTimeLoggedIn(LocalDateTime.now());
            user.setDeleted(false);

            user.setFirstName("Stoyan");
            user.setLastName("Imenski");

            user.setBornTown(sofia);
            user.setCurrentTown(sofia);


            this.userService.save(user);

            System.out.println(user.getFullName());

            Album album = new Album("SummerAlbum", "blue");
            album.setPrivate(true);
            album.setOwner(user);
            Picture picture =
                    new Picture("newPicture", "caption", "path");
            picture.getAlbums().add(album);

            this.pictureService.save(picture);


            album.getPictures().add(picture);

            user.getAlbums().add(album);
            this.albumService.save(album);

            this.userService.save(user);

            Country greece = new Country("Greece");
            Town athenes = new Town("Sofia");
            athenes.setCountry(greece);

            User user2 = new User();
            user2.setUsername("nuser");
            user2.setPassword("passwor#Dd123");
            user2.setEmail("adsds@abv.bg");
            user2.setBornTown(athenes);
            user2.setCurrentTown(athenes);
            user2.setAge(33);
            user2.setFirstName("Rocco");
            user2.setLastName("Georgiev");
            user2.setDeleted(false);
            user2.setRegisteredOn(LocalDateTime.now());
            user2.setLastTimeLoggedIn(LocalDateTime.now());

            this.userService.save(user2);

            user.getFriends().add(user2);


            System.out.println(user.getFullName());
            System.out.println(user2.getFullName());

            this.userService.save(user);
            this.userService.save(user2);

        }catch (Exception e){
            e.printStackTrace();
        }

    }




}