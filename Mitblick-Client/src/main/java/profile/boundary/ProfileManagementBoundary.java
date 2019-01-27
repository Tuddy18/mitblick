package profile.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import exception.BusinessException;
import profile.dto.FilterDTO;
import profile.dto.ProfileDTO;
import profile.service.ProfileManagementService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/manage-profiles")
public class ProfileManagementBoundary {

    @EJB
    private ProfileManagementService profileManagementService;

    @GET
    @Path("/get-all")
    public Response getAll(@Context SecurityContext securityContext) {

        try {
            List<ProfileDTO> allProfiles = profileManagementService.getAll();
            String allProfilesJson = new Gson().toJson(allProfiles);
            return Response.ok(allProfilesJson).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    @GET
    @Path("/get-by-id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id, @Context SecurityContext securityContext) {

        try {
            ProfileDTO profile = profileManagementService.getById(id);
            String profileStr = new Gson().toJson(profile);
            return Response.ok(profileStr).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/get-by-email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByEmail(@PathParam("email") String email, @Context SecurityContext securityContext) {

        try {
            ProfileDTO profile = profileManagementService.getByEmail(email);
            String profileStr = new Gson().toJson(profile);
            return Response.ok(profileStr).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/update")
    public Response update(ProfileDTO profileDTO, @Context HttpHeaders headers) {
        try {

            ProfileDTO profile = profileManagementService.update(profileDTO);
            String profileStr = new Gson().toJson(profile);
            return Response.ok(profileStr).build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getExceptionCode()).build();
        }
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(final ProfileDTO profileDTO) {

        try {
            profileManagementService.create(profileDTO);
            return Response.status(Response.Status.CREATED).build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getExceptionCode()).build();
        }
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(final ProfileDTO profileDTO) {

        try {
            profileManagementService.delete(profileDTO);
            return Response.ok().build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getExceptionCode()).build();
        }
    }

    @POST
    @Path("/add-skill")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSkill(@FormParam("skillId") Long skillId,
                             @FormParam("skillAreaName") String skillAreaName,
                             @FormParam("skillRating") Integer skillRating,
                             @FormParam("email") String email,
                             @Context HttpHeaders headers) {
        try {
            profileManagementService.addSkill(skillId, skillAreaName, skillRating, email);
            return Response.ok().build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getExceptionCode()).build();
        }
    }

    @POST
    @Path("/add-projekt")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProjekt(@FormParam("projektName") String projektName,
                               @FormParam("email") String email,
                               @Context HttpHeaders headers) {
        try {
            profileManagementService.addProjekt(projektName, email);
            return Response.ok().build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getExceptionCode()).build();
        }
    }

    @POST
    @Path("/remove-skill")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSkill(@FormParam("skillId") Long skillId,
                                @FormParam("email") String email,
                                @Context HttpHeaders headers) {
        try {
            ProfileDTO profile = profileManagementService.removeSkill(skillId, email);
            String profileStr = new Gson().toJson(profile);
            return Response.ok(profileStr).build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getExceptionCode()).build();
        }
    }

    @POST
    @Path("/remove-projekt")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeProjekt(@FormParam("projektName") String projektName,
                                  @FormParam("email") String email,
                                  @Context HttpHeaders headers) {
        try {
            profileManagementService.removeProjekt(projektName, email);
            return Response.ok().build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getExceptionCode()).build();
        }
    }
    @POST
    @Path("/accept")
    @Consumes({"application/x-www-form-urlencoded"})
    public Response acceptProfile(@FormParam("supervisorEmail") String supervisorEmail,@FormParam("userEmail") String userEmail, @Context HttpHeaders headers) {
        try {
            profileManagementService.acceptProfile(supervisorEmail,userEmail);
            return Response.ok().build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getExceptionCode()).build();
        }
    }

    @GET
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public String filter(@QueryParam("index") int index, @QueryParam("amount") int amount, @QueryParam("criteriaName") List<String> criteriaNames, @QueryParam("criteriaValues") List<String> criteriaValues, @QueryParam("skillId") List<Long> skillIDs) {


        try {
            FilterDTO filterDTO = profileManagementService.filter(index, amount, criteriaNames, criteriaValues, skillIDs);
            return new ObjectMapper().writeValueAsString(filterDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "not working because json exception";
        } catch (BusinessException e) {
            e.printStackTrace();
            return "not working because business exception";
        }

    }


}
