package com.tangovideos.services.nameParsing;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.tangovideos.models.VideoAndDancer;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class NameAwareNameParser implements NameExtractor {


    private final Set<String> knownDancers;
    private final Map<Set<String>, Pattern> knownCouplesNamesPatterns;
    private final BasicNameParser basicNameParser;


    // Currently hardcoded, but this will come from the DB.
    private final Map<String, Set<String>> pseudonyms = ImmutableMap.<String, Set<String>>builder()
            .put("Chicho Frumboli", ImmutableSet.of("Mariano Frumboli"))
            .put("MARIANO \"CHICHO\" FRÚMBOLI", ImmutableSet.of("Mariano Frumboli"))
            .put("Kalganova Eleonora", ImmutableSet.of("Eleonora Kalganova"))
            .put("Hermanos Macana", ImmutableSet.of("Guillermo De Fazio", "Enrique De Fazio"))
            .put("Carlitos Espinoza", ImmutableSet.of("Carlos Espinoza"))
            .put("Сергей Сиротюк", ImmutableSet.of("Sergey Sirotyuk"))
            .put("Kalganova Eoelonora", ImmutableSet.of("Eleonora Kalganova"))
            .put("Анна Зеленова", ImmutableSet.of("Anna Zelenova"))
            .put("Елена Кузнецова", ImmutableSet.of("Elena Kuznetsova"))
            .put("Максим Извеков", ImmutableSet.of("Maxim Izvekov"))
            .put("Александр Манясев", ImmutableSet.of("Alexander Manyasev"))
            .put("Новикова Анна", ImmutableSet.of("Anna Novikova"))

            .build();


    private final Set<String> notDancers = ImmutableSet.<String>builder()
            .add("Orq Bassan")
            .add("Milonga Bassan")
            .add("Orlando Reyes")
            .add("Viginia Pandolfi")
            .build();

    public NameAwareNameParser(Set<Set<String>> knownCouples) {

        this.basicNameParser = new BasicNameParser();
        knownDancers = knownCouples.stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        knownCouplesNamesPatterns = knownCouples.stream()
                .filter(dancers -> dancers.size() == 2)
                .collect(Collectors.toMap(
                        couple -> couple,
                        couple -> {
                            final Object[] c = couple.toArray();
                            final String dancer1 = getFirstName(c[0].toString()).toLowerCase();
                            final String dancer2 = getFirstName(c[1].toString()).toLowerCase();
                            return Pattern.compile(
                                    "(" + dancer1 + " (and|y|&|e|et|Y) " + dancer2 + ")" +
                                            "|(" + dancer2 + " (and|y|&|e|et|Y) " + dancer1 + ")");
                        }));


    }

    public static String getFirstName(String fullname) {
        return (fullname + " ").split(" ")[0];
    }


    public Set<String> extractNames(String label_) {
        final String label = StringUtils.stripAccents(label_).replace("  ", " ").toLowerCase();
        final Set<String> result = Sets.newHashSet();
        result.addAll(knownDancers.stream().filter(
                dancer -> label.contains(StringUtils.stripAccents(dancer).toLowerCase())
        ).collect(Collectors.toSet()));

        final List<String> pseudonyms = this.pseudonyms.entrySet().stream().filter(e -> label.contains(e.getKey().toLowerCase()))
                .map(Map.Entry::getValue)
                .flatMap(Set::stream)
                .collect(Collectors.toList());

        result.addAll(pseudonyms);
        final Set<String> firstNameMatches = knownCouplesNamesPatterns.entrySet().stream()
                .filter(dancersToPattern -> dancersToPattern.getValue().matcher(label).find())
                .map(Map.Entry::getKey)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        result.addAll(firstNameMatches);

        return result;

    }

    public static Set<String> filterDuplicateNames(Set<String> dancers) {
        final Set<String> duplicateNames = dancers.stream().filter(dancer -> {
            String firstName = getFirstName(dancer);
            return dancer.equals(firstName + " " + firstName);
        }).map(NameAwareNameParser::getFirstName).collect(Collectors.toSet());

        for (String name : duplicateNames) {
            dancers = dancers.stream()
                    .filter(dancer -> !dancer.contains(" " + name))
                    .collect(Collectors.toSet());
        }

        return dancers;
    }

    @Override
    public Set<String> extractNames(String title, String description) {
        Set<String> result = Sets.newHashSet();
        result.addAll(extractNames(title));

        result.addAll(extractNames(description));
        if (result.size() < 2) {
            result.addAll(basicNameParser.extractNames(title, description));
        }


        result.removeAll(notDancers);
        if (result.size() == 0) {
            result.add("Various dancers");
        }

        result = filterDuplicateNames(result);

        return result;
    }

    @Override
    public Set<String> extractNames(VideoAndDancer videoAndDancer) {
        return extractNames(videoAndDancer.getTitle(), videoAndDancer.getDescription());
    }

    @Override
    public String getLabel() {
        return "NameAwareNameParser";
    }
}
