package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.PetRequest;
import com.aruiz.user.notification.controller.dto.PetRequestUpdate;
import com.aruiz.user.notification.controller.dto.PetResponse;
import com.aruiz.user.notification.entity.PetEntity;
import com.aruiz.user.notification.entity.UserEntity;
import com.aruiz.user.notification.repository.PetRepository;
import com.aruiz.user.notification.repository.UserRepository;
import com.aruiz.user.notification.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public PetResponse save(Long ownerId, PetRequest petRequest) throws Exception {

        Optional<UserEntity> optionalUserEntity = userRepository.findById(ownerId);

        if (optionalUserEntity.isPresent()) {

            PetEntity petEntity = modelMapper.map(petRequest, PetEntity.class);

            petEntity.setOwner(optionalUserEntity.get());

            petRepository.save(petEntity);

            log.info("Saving pet entity with ID and NAME -> {} {}", petEntity.getId(), petEntity.getName());

            return modelMapper.map(petEntity, PetResponse.class);

        } else {
            log.error("User with id {} not found!!!!", ownerId);
        }

        log.error("There was an error saving the pet!!!!");
        return null;
    }

    @Override
    public List<PetResponse> findAll() throws Exception {

        List<PetEntity> petEntities = petRepository.findAll();

        if (petEntities.isEmpty()) {
            log.error("There are no entities to recover!!!");
            throw new Exception();
        }

        List<PetResponse> petResponseList = new ArrayList<>();

        for(PetEntity pet : petEntities) {
            log.info("Recovering Pets...");
            petResponseList.add(modelMapper.map(pet, PetResponse.class));
        }

        return petResponseList;
    }

    @Override
    public PetResponse findById(Long id) throws Exception {

        Optional<PetEntity> petEntityOptional = petRepository.findById(id);

        if (petEntityOptional.isPresent()) {

            log.info("Pet with ID " + id + " is present.");

            return modelMapper.map(petEntityOptional.get(), PetResponse.class);

        }

        log.error("Pet with ID -> {} not found!!!", id);
        throw new Exception();
    }

    @Override
    public String deleteById(Long id) throws Exception {

        Optional<PetEntity> optionalPetEntity = petRepository.findById(id);

        if (optionalPetEntity.isPresent()) {
            petRepository.deleteById(id);

            log.info("Pet successfully deleted");
            return "Pet with ID -> " + id + " successfully deleted!!";
        }

        log.error("Pet with ID {} not found", id);
        throw new Exception();
    }

    @Override
    public PetResponse updateById(Long id, PetRequestUpdate petRequestUpdate) throws Exception {

        Optional<PetEntity> optionalPetEntity = petRepository.findById(id);

        if (optionalPetEntity.isPresent()) {

            if (petRequestUpdate.getOwner() == null) {
                petRequestUpdate.setOwner(optionalPetEntity.get().getOwner());
            }

            if (petRequestUpdate.getIdentificationCode() == null) {
                petRequestUpdate.setIdentificationCode(optionalPetEntity.get().getIdentificationCode());
            }

            if (petRequestUpdate.getName() == null) {
                petRequestUpdate.setName(optionalPetEntity.get().getName());
            }

            if (petRequestUpdate.getDescription() == null) {
                petRequestUpdate.setDescription(optionalPetEntity.get().getDescription());
            }

            if (petRequestUpdate.getVaccinationData() == null) {
                petRequestUpdate.setVaccinationData(optionalPetEntity.get().getVaccinationData());
            }

            if (petRequestUpdate.getImg() == null) {
                petRequestUpdate.setImg(optionalPetEntity.get().getImg());
            }

            if (petRequestUpdate.getBirthdate() == null) {
                petRequestUpdate.setBirthdate(petRequestUpdate.getBirthdate());
            }

            if (petRequestUpdate.getMedication() == null) {
                petRequestUpdate.setMedication(petRequestUpdate.getMedication());
            }

            PetEntity petEntitySave = modelMapper.map(petRequestUpdate, PetEntity.class);

            petEntitySave.setId(id);

            petRepository.save(petEntitySave);

            log.info("Pet successfully updated!!!");

            return modelMapper.map(petEntitySave, PetResponse.class);
        }

        log.error("Pet with ID {} is not present!!!", id);
        throw new Exception();
    }

    @Override
    public void addPetImg(Long id, MultipartFile imageFile) throws IOException {

        Optional<PetEntity> petEntityOptional = petRepository.findById(id);

        if (petEntityOptional.isPresent()) {
            log.info("Saving pet image...");

            PetEntity petEntity = petEntityOptional.get();

            petEntity.setImg(Base64.getEncoder().encodeToString(imageFile.getBytes()));

            petRepository.save(petEntity);

        } else {
            throw new IOException();
        }

    }

    @Override
    public Optional<PetEntity> findByIdentificationCode(String identificationCode) throws Exception {
        return Optional.empty();
    }
}
