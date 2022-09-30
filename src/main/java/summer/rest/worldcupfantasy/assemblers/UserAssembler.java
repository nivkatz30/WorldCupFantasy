package summer.rest.worldcupfantasy.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import summer.rest.worldcupfantasy.dto.UserDTO;
import summer.rest.worldcupfantasy.controllers.UserController;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class UserAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {


    @Override
    public EntityModel<UserDTO> toModel(UserDTO entity) {
        try {
            return EntityModel.of(entity,
                    linkTo(methodOn(UserController.class).getUser(entity.getUser().getUserId())).withSelfRel());
        } catch (ApiErrorResponse e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CollectionModel<EntityModel<UserDTO>> toCollectionModel(Iterable<? extends UserDTO> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
