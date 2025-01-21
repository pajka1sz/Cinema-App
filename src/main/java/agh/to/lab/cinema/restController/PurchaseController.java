package agh.to.lab.cinema.restController;

import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.purchases.PurchaseDTO;
import agh.to.lab.cinema.model.purchases.PurchaseService;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.seances.SeanceService;
import agh.to.lab.cinema.model.users.CinemaUser;
import agh.to.lab.cinema.model.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final UserService userService;
    private final SeanceService seanceService;
    private static final String baseUrl = "http://localhost:8080/purchase";
    public static String getBaseUrl() {
        return baseUrl;
    }

    public PurchaseController(PurchaseService purchaseService, UserService userService, SeanceService seanceService) {
        this.purchaseService = purchaseService;
        this.userService = userService;
        this.seanceService = seanceService;
    }

    @GetMapping
    public List<Purchase> getPurchases() {
        return purchaseService.getPurchases();
    }

    @GetMapping(value = "/{id}")
    public Purchase getPurchase(@PathVariable Integer id) {
        return purchaseService.getPurchase(id);
    }

    @GetMapping(value = "/seance_id/{seance_id}")
    public List<Purchase> getPurchasesOfSeance(@PathVariable Long seance_id) {
        return purchaseService.getPurchasesOfSeance(seance_id);
    }

    @GetMapping(value = "/user_id/{user_id}")
    public List<Purchase> getPurchasesOfUser(@PathVariable Long user_id) {
        return purchaseService.getPurchasesOfUser(user_id);
    }

    @GetMapping(value = "/movie_id/{movie_id}")
    public List<Purchase> getPurchasesOfMovie(@PathVariable Integer movie_id) {
        return purchaseService.getPurchasesOfMovie(movie_id);
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<Optional<Purchase>> addPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        CinemaUser user = userService.getUser(purchaseDTO.getUser_id());
        if (user == null) {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.BAD_REQUEST);
        }
        Seance seance = seanceService.getSeance(purchaseDTO.getSeance_id());
        Purchase purchase = new Purchase(
                purchaseDTO.getNumberOfTickets(),
                user,
                seance
        );
        purchaseService.addPurchase(purchase);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", consumes = "application/json")
    public ResponseEntity<String> deletePurchase(@PathVariable Integer id) {
        Purchase purchase = purchaseService.getPurchase(id);
        if (purchase.getSeance().getStartDate().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>("You cannot delete reservation of already watched seance!", HttpStatus.BAD_REQUEST);
        }
        purchaseService.deletePurchase(id);
        return new ResponseEntity<>("Purchase deleted.", HttpStatus.OK);
    }
}
