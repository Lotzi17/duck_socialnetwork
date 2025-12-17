package lab2_map.service;

import lab2_map.domain.*;
import lab2_map.repo.*;
import lab2_map.util.Page;

import java.util.List;
import java.util.Set;

public class SocialNetworkService {

    private final UserDBRepository userRepo;
    private final FriendshipDBRepository friendshipRepo;
    private final MessageDBRepository messageRepo;
    private final EventDBRepository eventRepo;
    private final EventSubscriptionDBRepository subscriptionRepo;
    private final RaceEventDBRepository raceRepo;
    private final CardDBRepository cardRepo;
    private final CardMembersDBRepository cardMembersRepo;

    public SocialNetworkService(
            UserDBRepository userRepo,
            FriendshipDBRepository friendshipRepo,
            MessageDBRepository messageRepo,
            EventDBRepository eventRepo,
            EventSubscriptionDBRepository subscriptionRepo,
            RaceEventDBRepository raceRepo,
            CardDBRepository cardRepo,
            CardMembersDBRepository cardMembersRepo
    ) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.messageRepo = messageRepo;
        this.eventRepo = eventRepo;
        this.subscriptionRepo = subscriptionRepo;
        this.raceRepo = raceRepo;
        this.cardRepo = cardRepo;
        this.cardMembersRepo = cardMembersRepo;
    }

    // ============================================================
    // USERS
    // ============================================================

    public Long addUser(User u) {
        return userRepo.save(u);
    }

    public void deleteUser(Long id) {
        userRepo.delete(id);
    }

    public User findUser(Long id) {
        return userRepo.findOne(id);
    }

    // ============================================================
    // PAGED DUCKS (LAB 6 + LAB 7)
    // ============================================================

    public Page<Duck> getDucksPage(int page, int size, UserType filter) {
        return userRepo.findDucksOnPage(page, size, filter);
    }

    // ============================================================
    // FRIENDSHIPS
    // ============================================================

    public Long addFriendship(Long u1, Long u2) {
        return friendshipRepo.save(u1, u2);
    }

    public void deleteFriendship(Long id) {
        friendshipRepo.delete(id);
    }

    public List<Friendship> getAllFriendships() {
        return friendshipRepo.findAll(userRepo);
    }

    // ============================================================
    // COMMUNITIES
    // ============================================================

    public List<Set<User>> getAllCommunities() {
        List<User> all = friendshipRepo.getAllUsersWithFriends(userRepo);
        return friendshipRepo.computeCommunities(all, userRepo);
    }

    public int getCommunityCount() {
        // dacă nu există friendships, lista de comunități va fi goală → 0
        return getAllCommunities().size();
    }

    public Set<User> getMostSociableCommunity() {
        // friendshipRepo întoarce acum mereu o mulțime (eventual goală), niciodată null
        return friendshipRepo.biggestDiameterCommunity(userRepo);
    }
}
