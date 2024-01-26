package com.example.preorder.user.core.service;

import com.example.preorder.user.presentation.resources.request.UserChangePasswordDTO;
import com.example.preorder.user.presentation.resources.request.UserInfoEditDTO;
import com.example.preorder.user.presentation.resources.response.TokenDTO;
import com.example.preorder.user.presentation.resources.request.UserSignUpDTO;
import com.example.preorder.user.presentation.resources.response.UserInfoDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    TokenDTO signUp(UserSignUpDTO signUpDTO, MultipartFile file);

    TokenDTO reissueToken(String refreshToken);

    UserInfoDTO getUserInfo(Long userId);

    void sendAuthCode(String email);

    void logout(String accessToken, String refreshToken);

    void editPassword(UserChangePasswordDTO changePasswordDTO);

    void editInfo(UserInfoEditDTO infoEditDTO);
}
