// src/features/auth/authSlice.ts
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import * as authService from "../../services/authService";

interface AuthState {
  user: any | null;
  token: string | null;
  status: "idle" | "loading" | "succeeded" | "failed";
  error: string | null;
}

const initialState: AuthState = {
  user: null,
  token:
    typeof window !== "undefined" && typeof sessionStorage !== "undefined"
      ? sessionStorage.getItem("token")
      : null,
  status: "idle",
  error: null,
};

// helper đơn giản lấy message từ axios/other errors
const extractErrorMessage = (err: any) => {
  if (err?.response?.data?.message) return err.response.data.message;
  if (err?.message) return err.message;
  return "Lỗi không xác định";
};

/**
 * Thunk đăng nhập: trả về trực tiếp phần `data` từ response backend (AuthData)
 * nên component sẽ nhận action.payload là đối tượng chứa token/email/roles...
 */
export const loginUser = createAsyncThunk<
  authService.AuthData | null,
  { username: string; password: string },
  { rejectValue: string }
>("auth/login", async (payload, thunkAPI) => {
  try {
    const res = await authService.login(payload);
    // authService.login trả về toàn bộ response (code/message/data)
    return res?.data ?? null;
  } catch (err) {
    return thunkAPI.rejectWithValue(extractErrorMessage(err));
  }
});

export const registerUser = createAsyncThunk<
  authService.AuthData | null,
  authService.RegisterPayload,
  { rejectValue: string }
>("auth/register", async (payload, thunkAPI) => {
  try {
    const res = await authService.register(payload);
    return res?.data ?? null;
  } catch (err) {
    return thunkAPI.rejectWithValue(extractErrorMessage(err));
  }
});

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    logoutLocal(state) {
      state.user = null;
      state.token = null;
      state.status = "idle";
      state.error = null;
      authService.logout();
    },
    setUser(state, action: PayloadAction<{ user: any; token?: string }>) {
      state.user = action.payload.user;
      if (action.payload.token) {
        state.token = action.payload.token;
        try {
          sessionStorage.setItem("token", action.payload.token);
        } catch (err) {
          console.warn("Không thể lưu token vào sessionStorage", err);
        }
        authService.setAuthToken(action.payload.token);
      }
    },
  },
  extraReducers: (builder) => {
    // login
    builder.addCase(loginUser.pending, (state) => {
      state.status = "loading";
      state.error = null;
    });
    builder.addCase(loginUser.fulfilled, (state, action) => {
      state.status = "succeeded";
      state.error = null;

      // action.payload giờ là AuthData | null
      const payload = action.payload;
      if (payload?.token) {
        state.token = payload.token;
        // build a user object from available fields
        state.user = {
          username: payload.username ?? null,
          email: payload.email ?? null,
          roles: payload.roles ?? null,
          userId: payload.userId ?? null,
          refreshToken: payload.refreshToken ?? null,
        };

        // persist token to sessionStorage and set axios header
        authService.setAuthToken(payload.token);
      } else {
        // Nếu API trả thành công nhưng không kèm token (ví dụ chỉ message) - không thay đổi state.token
      }
    });
    builder.addCase(loginUser.rejected, (state, action) => {
      state.status = "failed";
      state.error = (action.payload as string) ?? "Đăng nhập thất bại";
    });

    // register
    builder.addCase(registerUser.pending, (state) => {
      state.status = "loading";
      state.error = null;
    });
    builder.addCase(registerUser.fulfilled, (state, action) => {
      state.status = "succeeded";
      state.error = null;

      const payload = action.payload;
      if (payload?.token) {
        state.token = payload.token;
        state.user = {
          username: payload.username ?? null,
          email: payload.email ?? null,
          roles: payload.roles ?? null,
          userId: payload.userId ?? null,
        };
        try {
          sessionStorage.setItem("token", payload.token);
          if (payload.email) sessionStorage.setItem("email", payload.email);
        } catch (err) {
          console.warn("Không thể lưu vào sessionStorage", err);
        }
        authService.setAuthToken(payload.token);
      }
    });
    builder.addCase(registerUser.rejected, (state, action) => {
      state.status = "failed";
      state.error = (action.payload as string) ?? "Đăng ký thất bại";
    });
  },
});

export const { logoutLocal, setUser } = authSlice.actions;
export default authSlice.reducer;
