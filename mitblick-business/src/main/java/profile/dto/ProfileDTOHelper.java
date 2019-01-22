package profile.dto;

import profile.entities.Profile;
import projekt.dto.ProjektDTOHelper;
import skills.dto.SkillDTOHelper;

public class ProfileDTOHelper {

    private ProfileDTOHelper() {
        //empty private constructor to hide the public implicit one
    }

    public static ProfileDTO fromEntity(Profile profile) {

        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setFirstName(profile.getFirstName());
        profileDTO.setLastName(profile.getLastName());
        profileDTO.setEmail(profile.getEmail());
        profileDTO.setPhoto(profile.getPhoto());
        profileDTO.setSkills(SkillDTOHelper.fromEntity(profile.getSkills()));
        profileDTO.setProjekts(ProjektDTOHelper.fromEntity(profile.getProjekts()));

        return profileDTO;
    }

    public static Profile toEntity(ProfileDTO profileDTO) {

        Profile profile = new Profile();

        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName(profileDTO.getLastName());
        profile.setEmail(profileDTO.getEmail());
        profile.setPhoto(profileDTO.getPhoto());
        profile.setSkills(SkillDTOHelper.toEntity(profileDTO.getSkills()));
        profile.setProjekts(ProjektDTOHelper.toEntity(profileDTO.getProjekts()));

        return profile;
    }

    public static Profile updateEntityWithDTO(Profile profile, ProfileDTO profileDTO) {
        profile.setEmail(profileDTO.getEmail());

        if (profileDTO.getFirstName() != null) {
            profile.setFirstName(profileDTO.getFirstName());
        }
        if (profileDTO.getLastName() != null) {
            profile.setLastName(profileDTO.getLastName());
        }
        if (profileDTO.getPhoto() != null) {
            profile.setPhoto(profileDTO.getPhoto());
        }
        if (profileDTO.getSkills() != null) {
            profile.setSkills(SkillDTOHelper.toEntity(profileDTO.getSkills()));
        }
        if (profileDTO.getProjekts() != null) {
            profile.setProjekts(ProjektDTOHelper.toEntity(profileDTO.getProjekts()));
        }
        return profile;
    }

}