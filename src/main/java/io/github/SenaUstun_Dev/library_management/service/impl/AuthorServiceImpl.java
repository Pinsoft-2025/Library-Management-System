package io.github.SenaUstun_Dev.library_management.service.impl;

import io.github.SenaUstun_Dev.library_management.exception.BaseException;
import io.github.SenaUstun_Dev.library_management.dto.request.CreateAuthorRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdateAuthorRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.AuthorResponse;
import io.github.SenaUstun_Dev.library_management.entity.Author;
import io.github.SenaUstun_Dev.library_management.exception.ErrorMessages;
import io.github.SenaUstun_Dev.library_management.repository.AuthorRepository;
import io.github.SenaUstun_Dev.library_management.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponse addAuthor(CreateAuthorRequest request) {
        Author author = Author.builder()
                .penName(request.penName())
                .firstName(request.firstName())
                .secondName(request.secondName())
                .build();

        authorRepository.save(author);

        return convertTorResponseDto(author);
    }

    @Transactional
    @Override
    public AuthorResponse updateAuthor(UpdateAuthorRequest request, Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new BaseException(
                HttpStatus.NOT_FOUND,
                ErrorMessages.AUTHOR_NOT_FOUND,
                "Author with ID " + id + " not found."
        ));

        author.setPenName(request.penName());
        author.setFirstName(request.firstName());
        author.setSecondName(request.secondName());

        authorRepository.save(author);
        return convertTorResponseDto(author);
    }


    @Transactional
    @Override
    public void deleteAuthor(Long id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
        }
        else{
            throw new BaseException(
                    HttpStatus.NOT_FOUND,
                    ErrorMessages.AUTHOR_NOT_FOUND,
                    "Author with ID " + id + " not found."
            );
        }
    }

    @Override
    public AuthorResponse findAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new BaseException(
                HttpStatus.NOT_FOUND,
                ErrorMessages.AUTHOR_NOT_FOUND,
                "Author with ID " + id + " not found."
        ));
        return convertTorResponseDto(author);
    }

    @Override
    public List<AuthorResponse> findAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        return authors.stream()
                .map(author -> convertTorResponseDto(author))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorResponse> findAuthorsByCriteria(String penName, String firstName, String secondName) {
        List<Author> authors = authorRepository.findByCriteria(penName, firstName, secondName);

        return authors.stream()
                .map(author -> convertTorResponseDto(author))
                .collect(Collectors.toList());
    }


    //>>>>>>>>>>>>>> HELPER METHODS

    private AuthorResponse convertTorResponseDto(Author author) {
        if (author == null) {
                throw new BaseException(HttpStatus.NOT_FOUND,
                        ErrorMessages.AUTHOR_NOT_FOUND,
                        "Author with ID " + author.getId() + " not found. Cant convert to AuthorResponseDto.");
            }
        return AuthorResponse.builder()
                .id(author.getId())
                .penName(author.getPenName())
                .firstName(author.getFirstName())
                .secondName(author.getSecondName())
                .build();
    }
}
