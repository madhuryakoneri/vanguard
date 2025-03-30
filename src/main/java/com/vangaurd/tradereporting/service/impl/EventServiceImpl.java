package com.vangaurd.tradereporting.service.impl;

import com.vangaurd.tradereporting.model.Event;
import com.vangaurd.tradereporting.repository.EventRepository;
import com.vangaurd.tradereporting.service.IEventService;
import com.vangaurd.tradereporting.service.IXmlParserService;
import com.vangaurd.tradereporting.utils.AnagramUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EventServiceImpl implements IEventService {

    @PersistenceContext
    private EntityManager entityManager;

    private final EventRepository eventRepository;
    private final IXmlParserService xmlParserService;

    @Override
    public void processXmlFile(String filePath) {
        try {
            log.info("Processing XML file: {}", filePath);
            List<Event> events = xmlParserService.parseXmlFile(filePath);
            log.debug("Number of events Parsed: {}", events.size());
            eventRepository.saveAll(events);
        } catch (Exception e) {
            log.error("Error processing events: {}", e.getMessage());
        }
    }

    @Override
    public List<Event> getFilteredEvents() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        // Adding the first condition: (SellerParty = "EMU_BANK" && PremiumCurrency = "AUD")
        Predicate condition1 = cb.and(
                cb.equal(root.get("sellerParty"), "EMU_BANK"),
                cb.equal(root.get("premiumCurrency"), "AUD")
        );

        // Adding the second condition: (SellerParty = "BISON_BANK" && PremiumCurrency = "USD")
        Predicate condition2 = cb.and(
                cb.equal(root.get("sellerParty"), "BISON_BANK"),
                cb.equal(root.get("premiumCurrency"), "USD")
        );

        predicates.add(cb.or(condition1, condition2));

        // If needed, additional conditions can be added dynamically in the future
        // Example: predicates.add(cb.and(...));

        query.select(root).where(predicates.toArray(new Predicate[0]));
        List<Event> result = entityManager.createQuery(query).getResultList();
        return result.stream().filter(event -> !AnagramUtil.isAnagram(event.getSellerParty(), event.getBuyerParty())).toList();
    }
}
