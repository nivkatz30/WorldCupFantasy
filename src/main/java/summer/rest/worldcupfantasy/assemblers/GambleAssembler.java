package summer.rest.worldcupfantasy.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import summer.rest.worldcupfantasy.controllers.GambleController;
import summer.rest.worldcupfantasy.controllers.UserController;
import summer.rest.worldcupfantasy.dto.GambleDTO;
import summer.rest.worldcupfantasy.dto.UserDTO;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class add locations links to Gamble class presentation.
 */
@Component
public class GambleAssembler implements RepresentationModelAssembler<GambleDTO, EntityModel<GambleDTO>> {

    /**
     * This method convert a Gamble DTO object to entity model of Gamble DTO with the necessary links.
     * @param entity
     * @return
     */
    @Override
    public EntityModel<GambleDTO> toModel(GambleDTO entity) {
        try {
            return EntityModel.of(entity,
                    linkTo(methodOn(GambleController.class).getGambleById(entity.getGamble().getGambleId())).withSelfRel(),
                    linkTo(methodOn(GambleController.class).getAllGambles()).withRel("allGambles"));
        } catch (ApiErrorResponse e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method convert an iterable of Gamble DTO object to collection model of entity model of Gamble DTO with the necessary links.
     * @param entities
     * @return
     */
    @Override
    public CollectionModel<EntityModel<GambleDTO>> toCollectionModel(Iterable<? extends GambleDTO> entities) {
        return CollectionModel.of(StreamSupport.stream(entities.spliterator(),false).map(this::toModel).collect(Collectors.toList()))
                .add(linkTo(methodOn((GambleController.class)).getAllGambles()).withSelfRel());
    }
}
