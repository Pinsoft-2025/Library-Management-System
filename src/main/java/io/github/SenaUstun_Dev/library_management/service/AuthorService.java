package io.github.SenaUstun_Dev.library_management.service;

import io.github.SenaUstun_Dev.library_management.dto.request.CreateAuthorRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdateAuthorRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.AuthorResponse;

import java.util.List;

public interface AuthorService {
    public AuthorResponse addAuthor(CreateAuthorRequest request);
    public AuthorResponse updateAuthor(UpdateAuthorRequest request, Long id );
    public void deleteAuthor(Long id);

    public AuthorResponse findAuthorById(Long id);
    public List<AuthorResponse> findAllAuthors();
    public List<AuthorResponse> findAuthorsByCriteria(String penName, String firstName, String secondName);

}
