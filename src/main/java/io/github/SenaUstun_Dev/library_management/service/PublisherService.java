package io.github.SenaUstun_Dev.library_management.service;

import io.github.SenaUstun_Dev.library_management.dto.request.CreatePublisherRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdatePublisherRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.PublisherResponse;

import java.util.List;

public interface PublisherService {
    public PublisherResponse addPublisher(CreatePublisherRequest request);
    public PublisherResponse updatePublisher(UpdatePublisherRequest request, Long id );
    public void deletePublisher(Long id);

    public PublisherResponse findPublisherById(Long id);
    public List<PublisherResponse> findAllPublisher();
}
