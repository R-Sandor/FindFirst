import './globals.css'
import type { Metadata } from 'next'
import Navbar from "../componenets/Navbar"
import 'bootstrap/dist/css/bootstrap.css'

import { Inter } from 'next/font/google'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'FindFirst',
  description: 'Helping you find it first!',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>
        <Navbar />
        {children}
      </body>
    </html>
  )
}
