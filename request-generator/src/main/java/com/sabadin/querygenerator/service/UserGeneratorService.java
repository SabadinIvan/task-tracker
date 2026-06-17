package com.sabadin.querygenerator.service;

import com.sabadin.querygenerator.model.AuthResponse;
import com.sabadin.querygenerator.model.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@AllArgsConstructor
public class UserGeneratorService {

    public static void main(String[] args) {
        System.out.println(NAMES.size());
        System.out.println(FAMILIES.size());
    }

    private static final long CACHE_TTL_MINUTES = 10;
    private static final String USER_REGISTRATION_URL = "/api/auth/register";
    private static final int MAX_COUNT_THREADS = 50;

    private static final List<String> NAMES = List.of(
            "James", "Emma", "Liam", "Sophia", "Noah", "Olivia", "Mateo", "Mia", "Lucas", "Isabella", "Ethan",
            "Amelia", "Alejandro", "Camila", "Muhammad", "Fatima", "Dmitri", "Anastasia", "Wei", "Li", "Kenji",
            "Yuki", "Ravi", "Priya", "Carlos", "Luz", "Jean", "Marie", "Giuseppe", "Sofia", "Hans", "Ingrid",
            "Omar", "Layla", "Ivan", "Olga", "Chen", "Mei", "Hiroshi", "Sakura", "Raj", "Anjali", "Diego", "Ksenia",
            "Valentina", "Anton", "Vera", "Jin", "Sun", "Björn", "Freya", "Lars", "Tove", "Piotr", "Zofia",
            "Andrei", "Nadia", "Ahmed", "Zara", "Kwame", "Amina", "Pablo", "Elena", "Sergio", "Clara", "Stefan",
            "Irina", "Viktor", "Kateryna", "Mohammed", "Aisha", "Chang", "Liu", "Takumi", "Rin", "Arjun",
            "Kavita", "Nikolai", "Svetlana", "Javier", "Carmen", "Manuel", "Rosa", "Yusuf", "Leila", "Emil",
            "Marta", "Gustav", "Astrid", "Dario", "Luciana", "Milan", "Teodora", "Bogdan", "Daria", "Rafael", "Ines", "Oscar", "Renata");

    private static final List<String> FAMILIES = List.of(
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Martinez", "Rodriguez", "Lopez",
            "Hernandez", "Gonzalez", "Perez", "Sanchez", "Ramirez", "Torres", "Flores", "Rivera", "Gomez",
            "Diaz", "Cruz", "Müller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner", "Becker",
            "Hoffmann", "Schäfer", "Ivanov", "Smirnov", "Petrov", "Sokolov", "Kuznetsov", "Popov", "Volkov",
            "Kozlov", "Novikov", "Morozov", "Wang", "Li", "Zhang", "Liu", "Chen", "Yang", "Huang", "Zhao",
            "Zhou", "Wu", "Kim", "Park", "Lee", "Choi", "Jung", "Kang", "Cho", "Yoon", "Lim", "Bae", "Suzuki",
            "Tanaka", "Takahashi", "Nakamura", "Watanabe", "Ito", "Yamamoto", "Kobayashi", "Sato", "Kato",
            "Rossi", "Ferrari", "Esposito", "Romano", "Colombo", "Ricci", "Marino", "Greco", "Bruno", "Gallo",
            "Fernandez", "Ruiz", "Alvarez", "Silva", "Costa", "Oliveira", "Pereira", "Santos", "Sousa", "Lima",
            "Carvalho", "Kowalski", "Wiśniewski", "Dąbrowski", "Lewandowski", "Wójcik", "Kamiński", "Krawczyk", "Nowak");

    private final RestClient restClient;

    public String generateUser() {
        log.info("called UserGeneratorService -> generateUser");
        Random random = new Random();
        int index = random.nextInt(1000);
        int nameIndex = random.nextInt(99);
        int familyIndex = random.nextInt(99);
        String firstName = NAMES.get(nameIndex);
        String lastName = FAMILIES.get(familyIndex);
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);
        registerRequest.setEmail(firstName + "." + lastName + "." + index + "@gmail.com");
        registerRequest.setPassword("Q1w2e3r4");
        registerRequest.setLogonName(firstName + lastName + index);
        registerRequest.setRole("ROLE_USER");

        log.info("============ Formed register request ============");
        log.info("registerRequest -> " + registerRequest);
        log.info("============ Formed register request ============");

        AuthResponse response = restClient
                .post()
                .uri(USER_REGISTRATION_URL)
                .body(registerRequest)
                .retrieve()
                .body(AuthResponse.class);
        log.info("response: token -> {}", response.getToken());
        return "A new user has been registered.";
    }

    public List<String> generateUsers(int count) {
        log.info("Called UserGeneratorService -> generateUsers; count {}", count);

        long start = System.nanoTime();
        List<RegisterRequest> registerRequestList = fillRequestList(count);
        List<String> tokens = sendRequestToUserRegister(registerRequestList);
        long end = System.nanoTime();
        log.info("Lead time: {} ms.", (end - start)/1_000_000);


        return tokens;
    }

    private List<RegisterRequest> fillRequestList(int count) {
        Random random = new Random();
        List<RegisterRequest> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(1000);
            int nameIndex = random.nextInt(99);
            int familyIndex = random.nextInt(99);
            String firstName = NAMES.get(nameIndex);
            String lastName = FAMILIES.get(familyIndex);
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName(firstName);
            registerRequest.setLastName(lastName);
            registerRequest.setEmail(firstName + "." + lastName + "." + index + "@gmail.com");
            registerRequest.setPassword("Q1w2e3r4");
            registerRequest.setLogonName(firstName + lastName + index);
            registerRequest.setRole("ROLE_USER");

            result.add(registerRequest);
        }
        return result;
    }

    private List<String> sendRequestToUserRegister(List<RegisterRequest> registerRequestList) {
        List<String> result = new ArrayList<>();
        int threadPoolSize = Math.min(MAX_COUNT_THREADS, registerRequestList.size());
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        try {
            List<CompletableFuture<String>> futures = registerRequestList.stream()
                    .map(registerRequest -> CompletableFuture.supplyAsync(() -> {
                                AuthResponse response = restClient.post()
                                        .uri(USER_REGISTRATION_URL)
                                        .body(registerRequest)
                                        .retrieve()
                                        .body(AuthResponse.class);
                                return response.getToken();
                            }, executor)
                            .exceptionally(throwable -> {
                                log.error("Ошибка: {}", throwable.getMessage());
                                return null;
                            })).toList();
            result = futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull).toList();
        } finally {
            executor.shutdown();
        }
        return result;
    }
}
