package summer.rest.worldcupfantasy.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import summer.rest.worldcupfantasy.dto.UserDTO;
import summer.rest.worldcupfantasy.controllers.UserController;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class add locations links to User class presentation.
 */
@Component
public class UserAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    /**
     * This method convert a User DTO object to entity model of User DTO with the necessary links.
     * @param entity
     * @return
     */
    @Override
    public EntityModel<UserDTO> toModel(UserDTO entity) {
        try {
            return EntityModel.of(entity,
                    linkTo(methodOn(UserController.class).getUser(entity.getUser().getUserId())).withSelfRel(),
                    linkTo(methodOn(UserController.class).getAllUsers()).withRel("allUsers"));
        } catch (ApiErrorResponse e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method convert an iterable of User DTO object to collection model of entity model of User DTO with the necessary links.
     * @param entities
     * @return
     */
    @Override
    public CollectionModel<EntityModel<UserDTO>> toCollectionModel(Iterable<? extends UserDTO> entities) {
        return CollectionModel.of(StreamSupport.stream(entities.spliterator(),false).map(this::toModel).collect(Collectors.toList()))
                .add(linkTo(methodOn((UserController.class)).getAllUsers()).withSelfRel());
    }
}
