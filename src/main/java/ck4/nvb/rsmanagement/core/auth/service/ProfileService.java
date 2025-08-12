package ck4.nvb.rsmanagement.core.auth.service;

import ck4.nvb.rsmanagement.core.auth.io.ProfileRequest;
import ck4.nvb.rsmanagement.core.auth.io.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest profileRequest);

    ProfileResponse getProfile(String username);
}
