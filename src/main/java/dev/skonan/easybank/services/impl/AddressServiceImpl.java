package dev.skonan.easybank.services.impl;

import dev.skonan.easybank.dto.AddressDto;
import dev.skonan.easybank.models.Address;
import dev.skonan.easybank.repositories.AddressRepository;
import dev.skonan.easybank.services.AddressService;
import dev.skonan.easybank.validators.ObjectsValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ObjectsValidator<AddressDto> validator;

    @Override
    public Integer save(AddressDto dto) {
        validator.validate(dto);
        Address address = AddressDto.toEntity(dto);

        return addressRepository.save(address).getId();
    }

    @Override
    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(AddressDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto findById(Integer id) {
        return addressRepository.findById(id)
                .map(AddressDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No address was found with the provided ID : " + id));
    }

    @Override
    public void delete(Integer id) {
        addressRepository.deleteById(id);
    }
}
