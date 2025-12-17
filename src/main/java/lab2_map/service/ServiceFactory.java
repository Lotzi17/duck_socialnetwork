package lab2_map.service;

import lab2_map.connections.DatabaseConnection;
import lab2_map.repo.*;

import java.sql.Connection;

public class ServiceFactory {

    public static SocialNetworkService createSocialNetworkService() {

        // One single DB connection for all repositories
        Connection connection = DatabaseConnection.getConnection();

        // Repositories (all use same shared connection implicitly)
        UserDBRepository userRepo = new UserDBRepository();
        FriendshipDBRepository friendshipRepo = new FriendshipDBRepository();
        MessageDBRepository messageRepo = new MessageDBRepository();
        EventDBRepository eventRepo = new EventDBRepository();
        EventSubscriptionDBRepository subscriptionRepo = new EventSubscriptionDBRepository();
        RaceEventDBRepository raceRepo = new RaceEventDBRepository();
        CardDBRepository cardRepo = new CardDBRepository();
        CardMembersDBRepository cardMembersRepo = new CardMembersDBRepository(userRepo);

        // Build service (NO pagingRepo argument needed!)
        return new SocialNetworkService(
                userRepo,
                friendshipRepo,
                messageRepo,
                eventRepo,
                subscriptionRepo,
                raceRepo,
                cardRepo,
                cardMembersRepo
        );
    }
}
