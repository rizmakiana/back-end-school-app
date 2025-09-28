package com.unindra.service.impl;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.unindra.entity.District;
import com.unindra.entity.Regency;
import com.unindra.entity.Staff;
import com.unindra.model.request.RegisterStaffRequest;
import com.unindra.model.util.Role;
import com.unindra.repository.StaffRepository;
import com.unindra.security.BCrypt;
import com.unindra.service.DistrictService;
import com.unindra.service.RegencyService;
import com.unindra.service.StaffService;
import com.unindra.service.ValidationService;
import com.unindra.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    
    private final StaffRepository repository;

    private final ValidationService validationService;

    private final RegencyService regencyService;

    private final DistrictService districtService;

    @Override
    public void register(RegisterStaffRequest request, Locale locale) {
        validationService.validate(request);

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw ExceptionUtil.badRequest("password.not.equals", locale);
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(
                    Integer.parseInt(request.getBirthYear()),
                    request.getBirthMonth(),
                    Integer.parseInt(request.getBirthDate()));
        } catch (DateTimeException | NumberFormatException e) {
            throw ExceptionUtil.badRequest("invalid.date", locale);
        }

        Regency regency = regencyService.findById(request.getBirthPlaceRegency())
                .orElseThrow(() -> ExceptionUtil.notFound("regency.notfound", locale));

        District district = districtService.findById(request.getDistrictAddress())
                .orElseThrow(() -> ExceptionUtil.notFound("district.notfound", locale));

        if (isUsernameExists(request.getUsername())) {
            throw ExceptionUtil.badRequest("username.already.exists", locale);
        }
        if (isEmailExists(request.getEmail())) {
            throw ExceptionUtil.badRequest("email.already.exists", locale);
        }
        if (isPhoneNumberExists(request.getPhoneNumber())) {
            throw ExceptionUtil.badRequest("phone.number.already.exists", locale);
        }

        Staff staff = new Staff();

        staff.setName(request.getName());
        staff.setGender(request.getGender());
        staff.setBirthplace(regency);
        staff.setBirthDate(birthDate);
        staff.setDistrictAddress(district);
        staff.setAddress(request.getAddress());
        staff.setUsername(request.getUsername()); 
        staff.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        staff.setEmail(request.getEmail());
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setRole(Role.STAFF);

        repository.save(staff);
    }

    public boolean isUsernameExists(String username) {
        return repository.existsByUsername(username);
    }

    public boolean isPhoneNumberExists(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }

    public boolean isEmailExists(String email) {
        return repository.existsByEmail(email);
    }
    
}
