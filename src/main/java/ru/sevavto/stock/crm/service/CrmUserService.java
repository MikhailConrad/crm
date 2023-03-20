package ru.sevavto.stock.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.exception.NotFoundException;
import ru.sevavto.stock.crm.model.entity.CrmUser;
import ru.sevavto.stock.crm.repository.CrmUserRepository;

@Service
public class CrmUserService implements UserDetailsService {
    private final CrmUserRepository crmUserRepository;

    @Autowired
    public CrmUserService(CrmUserRepository crmUserRepository) {
        this.crmUserRepository = crmUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CrmUser crmUser = crmUserRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с логином " + email + " не найден"));
        return new User(crmUser.getEmail(), crmUser.getPassword(), crmUser.getAuthorities());
    }
}
