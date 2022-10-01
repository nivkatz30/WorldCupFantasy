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


@Component
public class UserAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {


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

    @Override
    public CollectionModel<EntityModel<UserDTO>> toCollectionModel(Iterable<? extends UserDTO> entities) {
        return CollectionModel.of(StreamSupport.stream(entities.spliterator(),false).map(this::toModel).collect(Collectors.toList()))
                .add(linkTo(methodOn((UserController.class)).getAllUsers()).withSelfRel());
    }
}
