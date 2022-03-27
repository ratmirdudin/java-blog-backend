package com.ratmirdudin.jblog_server;

import com.ratmirdudin.jblog_server.models.entities.*;
import com.ratmirdudin.jblog_server.models.enums.CategoryEnum;
import com.ratmirdudin.jblog_server.models.enums.RoleEnum;
import com.ratmirdudin.jblog_server.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBOperationRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void run(String... args) {

        Role roleUser = roleRepository.save(new Role(null, RoleEnum.ROLE_USER));
        Role roleModerator = roleRepository.save(new Role(null, RoleEnum.ROLE_MODERATOR));
        Role roleAdmin = roleRepository.save(new Role(null, RoleEnum.ROLE_ADMIN));

        User userRatmir = userRepository.save(new User(
                null,
                "Ratmir",
                "Dudin",
                "ratmir",
                "ratmir@mail.ru",
                passwordEncoder.encode("password"),
                true,
                Set.of(roleUser, roleAdmin)
        ));

        User userDmitriy = userRepository.save(new User(
                null,
                "Dmitriy",
                "Lobanov",
                "dmitriy",
                "dmitriy@mail.ru",
                passwordEncoder.encode("password"),
                false,
                Set.of(roleUser, roleModerator)
        ));

        User userIvan = userRepository.save(new User(
                null,
                "Ivan",
                "Ivanov",
                "ivan",
                "ivan@mail.ru",
                passwordEncoder.encode("password"),
                false,
                Set.of(roleUser)
        ));

        User userAdmin = userRepository.save(new User(
                null,
                "Admin",
                "Admin",
                "admin",
                "admin@mail.ru",
                passwordEncoder.encode("password"),
                true,
                Set.of(roleUser, roleAdmin)
        ));

        Category categoryJava = categoryRepository.save(new Category(null, CategoryEnum.JAVA));
        Category categorySpring = categoryRepository.save(new Category(null, CategoryEnum.SPRING));
        Category categorySql = categoryRepository.save(new Category(null, CategoryEnum.SQL));
        Category categoryAngular = categoryRepository.save(new Category(null, CategoryEnum.ANGULAR));
        Category categoryDocker = categoryRepository.save(new Category(null, CategoryEnum.DOCKER));
        Category categoryKubernetes = categoryRepository.save(new Category(null, CategoryEnum.KUBERNETES));
        Category categoryGithub = categoryRepository.save(new Category(null, CategoryEnum.GITHUB));

        int number = 0;
        Post postOne = postRepository.save(new Post(
                null,
                getRandomTitle(++number),
                getRandomContent(number),
                userRatmir,
                Set.of(categoryJava, categoryKubernetes)
        ));

        Post postTwo = postRepository.save(new Post(
                null,
                getRandomTitle(++number),
                getRandomContent(number),
                userRatmir,
                Set.of(categoryAngular)
        ));

        Post postThree = postRepository.save(new Post(
                null,
                getRandomTitle(++number),
                getRandomContent(number),
                userRatmir,
                Set.of(categorySpring)
        ));


        postRepository.saveAll(
                List.of(
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userRatmir,
                                Set.of(categorySpring)
                        ),
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userAdmin,
                                Set.of(categorySpring)
                        ),
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userRatmir,
                                Set.of(categorySpring)
                        ),
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userRatmir,
                                Set.of(categorySpring)
                        ),
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userRatmir,
                                Set.of(categorySpring)
                        ),
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userAdmin,
                                Set.of(categoryDocker)
                        ),
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userRatmir,
                                Set.of(categoryDocker, categoryGithub)
                        ),
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userRatmir,
                                Set.of()
                        ),
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userRatmir,
                                Set.of()
                        ),
                        new Post(
                                null,
                                getRandomTitle(++number),
                                getRandomContent(number),
                                userAdmin,
                                Set.of(categorySql)
                        )
                )
        );

        int numOfComment = 0;
        commentRepository.saveAll(
                List.of(
                        new Comment(
                                postOne,
                                userDmitriy,
                                getRandomComment(++numOfComment)
                        ),
                        new Comment(
                                postOne,
                                userIvan,
                                getRandomComment(++numOfComment)
                        ),
                        new Comment(
                                postOne,
                                userDmitriy,
                                getRandomComment(++numOfComment)
                        ),
                        new Comment(
                                postOne,
                                userRatmir,
                                getRandomComment(++numOfComment)
                        ),
                        new Comment(
                                postTwo,
                                userIvan,
                                getRandomComment(++numOfComment)
                        ),
                        new Comment(
                                postTwo,
                                userRatmir,
                                getRandomComment(++numOfComment)
                        ),
                        new Comment(
                                postTwo,
                                userDmitriy,
                                getRandomComment(++numOfComment)
                        ),
                        new Comment(
                                postThree,
                                userDmitriy,
                                getRandomComment(++numOfComment)
                        ),
                        new Comment(
                                postThree,
                                userDmitriy,
                                getRandomComment(++numOfComment)
                        ),
                        new Comment(
                                postThree,
                                userIvan,
                                getRandomComment(++numOfComment)
                        )
                )
        );


        System.out.println("----------------All Data loaded into Database----------------");
    }

    private String getRandomSentencesFromText(String text, int number, int min, int max) {
        if (text.endsWith(".")) {
            text = text.substring(0, text.length() - 1);
        }
        String[] split = text.split("\\. ");
        List<String> strings = Arrays.stream(split).collect(Collectors.toList());
        Collections.shuffle(strings);
        int length = rnd(min, max);
        int start = rnd(0, strings.size() - length);
        int end = start + length;
        List<String> subList = strings.subList(start, end);
        return number + ". " + String.join(". ", subList) + ".";
    }

    private int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    private String getRandomTitle(int number) {
        String text = "Vitae elementum curabitur vitae nunc sed velit dignissim." +
                " Eget dolor morbi non arcu risus quis varius." +
                " Sagittis purus sit amet volutpat consequat mauris nunc congue." +
                " Integer malesuada nunc vel risus commodo." +
                " Odio tempor orci dapibus ultrices in." +
                " Quisque sagittis purus sit amet." +
                " Vitae congue eu consequat ac felis." +
                " Urna porttitor rhoncus dolor purus non enim praesent elementum facilisis." +
                " Tortor condimentum lacinia quis vel." +
                " Tortor at risus viverra adipiscing." +
                " Non blandit massa enim nec dui nunc mattis enim ut.";
        return getRandomSentencesFromText(text, number, 1, 4);
    }

    private String getRandomContent(int number) {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                " Adipiscing bibendum est ultricies integer quis." +
                " Sagittis orci a scelerisque purus semper." +
                " Facilisis gravida neque convallis a cras semper auctor." +
                " Et leo duis ut diam quam nulla porttitor." +
                " Id cursus metus aliquam eleifend mi in nulla posuere." +
                " Sit amet consectetur adipiscing elit." +
                " In metus vulputate eu scelerisque felis imperdiet proin fermentum." +
                " Phasellus faucibus scelerisque eleifend donec." +
                " Nulla aliquet enim tortor at auctor." +
                " Ipsum nunc aliquet bibendum enim." +
                " Etiam sit amet nisl purus in mollis." +
                " Faucibus vitae aliquet nec ullamcorper sit." +
                " Mattis aliquam faucibus purus in massa tempor nec feugiat." +
                " Porttitor leo a diam sollicitudin tempor." +
                " Amet volutpat consequat mauris nunc congue nisi vitae suscipit." +
                " Pulvinar mattis nunc sed blandit libero volutpat sed cras ornare." +
                " Nulla facilisi morbi tempus iaculis urna." +
                " A pellentesque sit amet porttitor eget dolor.";
        return getRandomSentencesFromText(text, number, 10, 15);
    }

    private String getRandomComment(int number) {
        String text = "Aliquet sagittis id consectetur purus." +
                " Tincidunt eget nullam non nisi est sit." +
                " Tristique senectus et netus et malesuada fames ac turpis." +
                " Convallis a cras semper auctor neque." +
                " Scelerisque felis imperdiet proin fermentum." +
                " Convallis tellus id interdum velit." +
                " Nulla porttitor massa id neque aliquam vestibulum morbi blandit cursus." +
                " Tellus cras adipiscing enim eu turpis egestas pretium aenean." +
                " Tempor orci dapibus ultrices in iaculis nunc." +
                " Lectus arcu bibendum at varius vel pharetra vel turpis nunc." +
                " Lobortis mattis aliquam faucibus purus in." +
                " Nunc sed augue lacus viverra." +
                " Pulvinar neque laoreet suspendisse interdum consectetur libero id faucibus nisl." +
                " Nibh venenatis cras sed felis eget velit aliquet sagittis id." +
                " Sed turpis tincidunt id aliquet risus feugiat." +
                " Auctor elit sed vulputate mi sit amet mauris.";
        return getRandomSentencesFromText(text, number, 1, 6);
    }

}
