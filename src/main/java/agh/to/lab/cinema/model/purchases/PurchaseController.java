package agh.to.lab.cinema.model.purchases;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
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

    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<Optional<Purchase>> addPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        List<Purchase> purchasesAlreadyMade = purchaseService.getPurchasesOfSeance(purchaseDTO.getSeance().getId());
        // Room full
        if (purchasesAlreadyMade.size() >= purchaseDTO.getSeance().getRoom().getCapacity())
            return new ResponseEntity<>(Optional.empty(), HttpStatus.valueOf(405));
        Purchase purchase = new Purchase(
                purchaseDTO.getUser(),
                purchaseDTO.getSeance()
        );
        purchaseService.addPurchase(purchase);
        return new ResponseEntity<>(Optional.of(purchase), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}", consumes = "application/json")
    public ResponseEntity<String> deletePurchase(@PathVariable Integer id) {
        purchaseService.deletePurchase(id);
        return new ResponseEntity<>("Purchase deleted.", HttpStatus.OK);
    }
}
