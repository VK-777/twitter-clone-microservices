export interface User {
  id: number;
  firstName: string;
  email: string;
  bio?: string;
  location?: string;
  website?: string;
  profilePictureUrl?: string;
  coverPictureUrl?: string;
}

export interface RegisterRequest {
  firstName: string;
  email: string;
  password: string;
  confirmPassword: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface UpdateProfileRequest {
  bio?: string;
  location?: string;
  website?: string;
}
