package agh.to.lab.cinema.model.purchases;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    private PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public void addPurchase(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    public void deletePurchase(Integer id) {
        purchaseRepository.deleteById(id);
    }

    public Purchase getPurchase(Integer id) {
        return purchaseRepository.findById(id).orElseThrow(() -> new RuntimeException("Purchase not found."));
    }

    public List<Purchase> getPurchases() {
        return purchaseRepository.findAll();
    }

    public List<Purchase> getPurchasesOfSeance(Long seance_id) {
        List<Purchase> allPurchases = getPurchases();
        List<Purchase> seancePurchases = new ArrayList<>();

        for (Purchase purchase: allPurchases) {
            if (purchase.getSeance().getId().equals(seance_id))
                seancePurchases.add(purchase);
        }
        return seancePurchases;
    }

    public List<Purchase> getPurchasesOfUser(Long user_id) {
        List<Purchase> allPurchases = getPurchases();
        List<Purchase> userPurchases = new ArrayList<>();

        for (Purchase purchase: allPurchases) {
            if (purchase.getUser().getId().equals(user_id))
                userPurchases.add(purchase);
        }
        return userPurchases;
    }
}
