import "./globals.scss";
import "bootstrap-icons/font/bootstrap-icons.min.css";
import "react-toastify/dist/ReactToastify.css";
import type { Metadata } from "next";
import Navbar from "@components/Navbar/Navbar";
import { NavigationEvents } from "@components/navigation-events";
import { Suspense } from "react";
import { Providers } from "./providers";

export const metadata: Metadata = {
  title: "FindFirst",
  description: "Helping you find it first!",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <Providers>
          <Navbar />
          <Suspense>
            <NavigationEvents />
          </Suspense>
          {children}
        </Providers>
      </body>
    </html>
  );
}
