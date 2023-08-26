import './globals.css'
import type { Metadata } from 'next'
import Navbar from '@components/Navbar'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.min.css'
import {NavigationEvents} from '@components/navigation-events'

import { Inter } from 'next/font/google'
import { Component } from 'react'

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
        <NavigationEvents/>
        {children}
      </body>
    </html>
  )
}
