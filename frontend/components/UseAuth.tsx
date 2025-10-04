"use client";
import { useEffect, useState } from "react";
import authService, { AuthStatus } from "@services/auth.service";

interface UserAuth {
    status: AuthStatus;
    userId?: number | null;
    profileImage?: string | null;
}

export default function useAuth(): UserAuth {
    const [auth, setAuth] = useState<UserAuth>({
        status: AuthStatus.Unauthorized,
        userId: null,
        profileImage: null,
    });

    useEffect(() => {
        async function checkAuth() {
            const status = authService.getAuthorized();
            let userId: number | null = null;
            let profileImage: string | null = null;

            if (status === AuthStatus.Authorized) {
                const user = authService.getUser(); // your backend returns { id, profileImage }
                userId = user?.id || null;
                profileImage = user?.profileImage || null;
            }

            setAuth({ status, userId, profileImage });
        }

        checkAuth();
    }, []);

    return auth;
}
