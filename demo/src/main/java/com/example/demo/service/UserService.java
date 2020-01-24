package com.example.demo.service;

import com.example.demo.model.Profile;
import com.example.demo.model.SkillProfile;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.dto.DtoMapping;
import com.example.demo.service.dto.ProfileDTO;
import com.example.demo.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DtoMapping dtoMapping;

    @Autowired
    private ProfileService profileService;

    private UserDTO userDTO;

    public UserService(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
            .stream()
            .map(dtoMapping::userToDTO)
            .collect(Collectors.toList());
    }

    public UserDTO findById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return null;
        }
        return dtoMapping.userToDTO(user.get());
    }

    public UserDTO save(UserDTO userDTO) {
        User user = userRepository.save(dtoMapping.dtoToUser(userDTO));
        return dtoMapping.userToDTO(user);
    }

    public ProfileDTO updateProfile(ProfileDTO profileDTO) {
        return profileService.updateProfile(profileDTO);
    }

    public void deactivate(ProfileDTO profileDTO) {
        profileService.deactivate(profileDTO);
    }


    public List<UserDTO> findAllSupervisors() {
        return userRepository.findAll()
            .stream()
            .filter(user -> user.isSupervisor())
            .map(dtoMapping::userToDTO)
            .collect(Collectors.toList());
    }

    public void setAdminStatus(int id, boolean status) {
        UserDTO userDTO = findById(id);
        if (userDTO != null && userDTO.getAdmin() != status) {
            userDTO.setAdmin(status);
        }
    }
}
