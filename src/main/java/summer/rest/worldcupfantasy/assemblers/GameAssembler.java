package summer.rest.worldcupfantasy.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import summer.rest.worldcupfantasy.controllers.GameController;
import summer.rest.worldcupfantasy.controllers.UserController;
import summer.rest.worldcupfantasy.dto.UserDTO;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class add locations links to Game class presentation.
 */
@Component
public class GameAssembler implements RepresentationModelAssembler<Game, EntityModel<Game>> {

    /**
     * This method convert a Game object to entity model of Game with the necessary links.
     * @param entity
     * @return
     */
    @Override
    public EntityModel<Game> toModel(Game entity) {
        try {
            return EntityModel.of(entity,
                    linkTo(methodOn(GameController.class).getGameById(entity.getGameId())).withSelfRel(),
                    linkTo(methodOn(GameController.class).getAllGames()).withRel("allGames"));
        } catch (ApiErrorResponse e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method convert an iterable of Game object to collection model of entity model of Game with the necessary links.
     * @param entities
     * @return
     */
    @Override
    public CollectionModel<EntityModel<Game>> toCollectionModel(Iterable<? extends Game> entities) {
        return CollectionModel.of(StreamSupport.stream(entities.spliterator(),false).map(this::toModel).collect(Collectors.toList()))
                .add(linkTo(methodOn((GameController.class)).getAllGames()).withSelfRel());
    }
}
