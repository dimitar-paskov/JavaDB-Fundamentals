package softuni.usersystem.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.usersystem.entities.Picture;
import softuni.usersystem.repositories.PictureRepository;
import softuni.usersystem.services.PictureService;
@Service
public class PictureServiceImpl implements PictureService {
    private PictureRepository pictureRepository;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public void save(Picture picture) {
        this.pictureRepository.saveAndFlush(picture);
    }
}
