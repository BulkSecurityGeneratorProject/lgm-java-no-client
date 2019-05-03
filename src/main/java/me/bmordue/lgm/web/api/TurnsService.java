package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.domain.PlayerTurn;
import me.bmordue.lgm.domain.RulesProcessor;
import me.bmordue.lgm.repository.PlayerRepository;
import me.bmordue.lgm.repository.PlayerTurnRepository;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
import me.bmordue.lgm.security.IAuthenticationFacade;
import me.bmordue.lgm.service.mapper.PlayerTurnMapper;
import me.bmordue.lgm.service.mapper.TurnOutcomeMapper;
import me.bmordue.lgm.web.api.model.TurnOrders;
import me.bmordue.lgm.web.api.model.TurnResultsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class TurnsService {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private TurnOutcomeRepository turnOutcomeRepository;

    @Autowired
    private TurnOutcomeMapper turnOutcomeMapper;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerTurnRepository playerTurnRepository;

    @Autowired
    private PlayerTurnMapper playerTurnMapper;

    @Autowired
    private RulesProcessor rulesProcessor;

    void postOrders(Long id, TurnOrders turnOrders) throws AuthenticationException {
        String userLogin = authenticationFacade.getCurrentUserLogin()
            .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("bad auth"));
        Player player = playerRepository.findByName(userLogin)
            .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("bad auth"));
        PlayerTurn playerTurn = playerTurnMapper.turnOrdersToPlayerTurn(id, player, turnOrders);
        playerTurnRepository.save(playerTurn);
        rulesProcessor.process(); // this should become asynchronous
    }

    TurnResultsResponse getTurnResults(Long id) {
        return turnOutcomeMapper.turnOutcomeToTurnResultsResponse(turnOutcomeRepository.getOne(id));
    }
}