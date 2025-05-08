package io.github.SenaUstun_Dev.library_management.service.impl;

import io.github.SenaUstun_Dev.library_management.dto.request.CreatePublisherRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdatePublisherRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.PublisherResponse;
import io.github.SenaUstun_Dev.library_management.entity.Publisher;
import io.github.SenaUstun_Dev.library_management.exception.BaseException;
import io.github.SenaUstun_Dev.library_management.exception.ErrorMessages;
import io.github.SenaUstun_Dev.library_management.repository.PublisherRepository;
import io.github.SenaUstun_Dev.library_management.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    @Override
    public PublisherResponse addPublisher(CreatePublisherRequest request) {
        Publisher publisher = Publisher.builder()
                .name(request.name())
                .build();

        publisherRepository.save(publisher);

        return convertToResponseDto(publisher);
    }

    @Transactional
    @Override
    public PublisherResponse updatePublisher(UpdatePublisherRequest request, Long id) {
        Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new BaseException(
                HttpStatus.NOT_FOUND,
                ErrorMessages.PUBLISHER_NOT_FOUND,
                "Publisher with ID " + id + " not found. Can't update non existing publisher."
        ));

        publisher.setName(request.name());

        publisherRepository.save(publisher);
        return convertToResponseDto(publisher);
    }

    @Transactional
    @Override
    public void deletePublisher(Long id) {
        if (publisherRepository.existsById(id)) {
            publisherRepository.deleteById(id);
        } else {
            throw new BaseException(
                    HttpStatus.NOT_FOUND,
                    ErrorMessages.PUBLISHER_NOT_FOUND,
                    "Publisher with ID " + id + " not found. Can't delete non existing publisher."
            );
        }
    }

    @Override
    public PublisherResponse findPublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new BaseException(
                HttpStatus.NOT_FOUND,
                ErrorMessages.PUBLISHER_NOT_FOUND,
                "Publisher with ID " + id + " not found."
        ));
        return convertToResponseDto(publisher);
    }

    @Override
    public List<PublisherResponse> findAllPublishers() {
        List<Publisher> publishers = publisherRepository.findAll();

        return publishers.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }


    //>>>>>>>>>>>>>> HELPER METHODS

    private PublisherResponse convertToResponseDto(Publisher publisher) {
        if (publisher == null) {
            throw new BaseException(HttpStatus.NOT_FOUND,
                    ErrorMessages.PUBLISHER_NOT_FOUND,
                    "Publisher with ID " + publisher.getId() + " not found. Cant convert to PublisherResponseDto.");
        }
        return PublisherResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }
}
