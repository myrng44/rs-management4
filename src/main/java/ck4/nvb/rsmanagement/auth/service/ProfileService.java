package ck4.nvb.rsmanagement.auth.service;

import ck4.nvb.rsmanagement.auth.io.ProfileRequest;
import ck4.nvb.rsmanagement.auth.io.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest profileRequest);

    ProfileResponse getProfile(String username);
}
