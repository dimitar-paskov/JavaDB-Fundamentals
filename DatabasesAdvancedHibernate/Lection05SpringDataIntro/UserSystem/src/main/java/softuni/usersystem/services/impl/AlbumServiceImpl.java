package softuni.usersystem.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.usersystem.entities.Album;
import softuni.usersystem.repositories.AlbumRepository;
import softuni.usersystem.services.AlbumService;

@Service
public class AlbumServiceImpl implements AlbumService {

    private AlbumRepository albumRepository;

    @Autowired
    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public void save(Album album) {
        this.albumRepository.saveAndFlush(album);

    }
}
