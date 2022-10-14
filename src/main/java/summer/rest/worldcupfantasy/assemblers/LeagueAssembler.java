package summer.rest.worldcupfantasy.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import summer.rest.worldcupfantasy.controllers.LeagueController;
import summer.rest.worldcupfantasy.controllers.UserController;
import summer.rest.worldcupfantasy.dto.LeagueDTO;
import summer.rest.worldcupfantasy.dto.UserDTO;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class LeagueAssembler implements RepresentationModelAssembler<LeagueDTO, EntityModel<LeagueDTO>> {


    @Override
    public EntityModel<LeagueDTO> toModel(LeagueDTO entity) {
        try {
            return EntityModel.of(entity,
                    linkTo(methodOn(LeagueController.class).getSingleLeague(entity.getLeagueId())).withSelfRel(),
                    linkTo(methodOn(LeagueController.class).getAllLeagues()).withRel("allLeagues"));
        } catch (ApiErrorResponse e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CollectionModel<EntityModel<LeagueDTO>> toCollectionModel(Iterable<? extends LeagueDTO> entities) {
        return CollectionModel.of(StreamSupport.stream(entities.spliterator(),false).map(this::toModel).collect(Collectors.toList()))
                .add(linkTo(methodOn((LeagueController.class)).getAllLeagues()).withSelfRel());
    }
}
