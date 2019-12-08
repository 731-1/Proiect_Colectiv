package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.dto.ProfileDTO;
import com.example.demo.service.dto.ProfileDTOEntityMapper;
import com.example.demo.service.interfaces.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProfileService implements IProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ConsultingLevelRepository consultingLevelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    public List<Profile> findAll()
    {
        return profileRepository.findAll();
    }

    /**
     * Insert new profile into DB
     * @param profileDTO profile to be inserted
     * @return the profile dto
     * @author Miruna
     */
    @Override
    public ProfileDTO saveProfile(ProfileDTO profileDTO) {
        Profile profile = ProfileDTOEntityMapper.getProfileFromProfileDTO(profileDTO);
        profileRepository.save(profile);

        return ProfileDTOEntityMapper.getDTOFromProfile(profile);
    }

    /**
     * Update the profile
     * @param profileDTO the profile to be updated
     * @return profile dto
     * @author Miruna
     */
    @Override
    public ProfileDTO updateProfile(ProfileDTO profileDTO) {
        Profile profile = profileRepository.getOne(profileDTO.getId());
        Region region = regionRepository.getOne(profileDTO.getRegionId());
        ConsultingLevel consultingLevel = consultingLevelRepository.getOne(profileDTO.getConsultingLevelId());
        User user = userRepository.getOne(profileDTO.getUserId());
        byte[] image = profileDTO.getImageURL();

        profile.setRegion(region);
        profile.setConsultingLevel(consultingLevel);
        profile.setUser(user);
        profile.setImage(image);

        Map<Integer, Integer> skillIds = profileDTO.getSkillProfileIds();
        Set<SkillProfile> skillProfiles = new HashSet<>();

        for(Map.Entry<Integer, Integer> entry : skillIds.entrySet()) {
            Skill skill = skillRepository.getOne(entry.getKey());
            SkillProfile skillProfile = new SkillProfile();

            skillProfile.setSkill_id(skill);
            skillProfile.setProfile_id(profile);
            skillProfile.setLevel(entry.getValue());

            skillProfiles.add(skillProfile);
        }

        profile.setSkillProfiles(skillProfiles);
        profileRepository.flush();

        return ProfileDTOEntityMapper.getDTOFromProfile(profile);
    }

    /**
     * Returns one profile in order to be displayed
     * @param profileId the id of the profile to be displayed
     * @return the profile DTO
     * @author Miruna
     */
    @Override
    public ProfileDTO getProfile(Integer profileId) {
        Profile profile = profileRepository.getOne(profileId);

        return ProfileDTOEntityMapper.getDTOFromProfile(profile);
    }

    /**
     * Sets the status to false = inactive
     * @param profileId the id of the profile that will be "deleted"
     * @return the profile DTO
     * @author Miruna
     */
    @Override
    public ProfileDTO deleteProfile(Integer profileId) {
        Profile profile = profileRepository.getOne(profileId);
        profile.setStatus(false);
        profileRepository.flush();

        return ProfileDTOEntityMapper.getDTOFromProfile(profile);
    }
}