import "./globals.css";
import type { Metadata } from "next";
import Navbar from "@components/Navbar/Navbar";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.min.css";
import { NavigationEvents } from "@components/navigation-events";
import { Suspense } from "react";

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
        <Navbar />
        <Suspense>
          <NavigationEvents />
        </Suspense>
        {children}
      </body>
    </html>
  );
}
