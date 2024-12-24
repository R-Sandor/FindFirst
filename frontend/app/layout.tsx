import "./globals.scss";
import "bootstrap-icons/font/bootstrap-icons.min.css";
import "react-toastify/dist/ReactToastify.css";
import type { Metadata } from "next";
import Navbar from "@components/Navbar/Navbar";
import { NavigationEvents } from "@components/navigation-events";
import { Suspense } from "react";
import { Providers } from "./providers";
import ChildernProp from "@type/Common/ChildrenProp";

export const metadata: Metadata = {
  title: "FindFirst",
  description: "Your own personal Search Engine!",
};

export default function RootLayout({ children }: Readonly<ChildernProp>) {
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
