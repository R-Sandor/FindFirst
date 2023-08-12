"use client"
import Link from "next/link";
import {  Nav, NavDropdown, Form, Button, ButtonGroup } from 'react-bootstrap'
import Image from "next/image";
import React, { useState } from "react";
import Logo from "./Logo";
import NavItem from "./NavItem";


const MENU_LIST = [
  { text: "Home", href: "/" },
  { text: "Guide", href: "/guide" },
];
const Navbar = () => {
  const [navActive, setNavActive] = useState(null);
  const [activeIdx, setActiveIdx] = useState(-1);


    const handleLogoutClick = () => {
        logout()
        history.push('/')
    }

    const authButton = () => {
        if (currentUser === null) {
            return (
                <ButtonGroup>
                    <Button variant="secondary" as={Link} to="/login">Login</Button>
                    <Button variant="secondary" as={Link} to="/signup">Signup</Button>
                </ButtonGroup>
            )
                
        } else {
            return <Button variant="secondary" onClick={handleLogoutClick}>Logout</Button>
        }
    }

  return (
    <header>
      <nav className={`nav`}>
       <Logo />
        <div
          onClick={() => setNavActive(!navActive)}
          className={`nav__menu-bar`}
        >
          <div></div>
          <div></div>
          <div></div>
        </div>
        <div className={`${navActive ? "active" : ""} nav__menu-list`}>
          {MENU_LIST.map((menu, idx) => (
            <div
              onClick={() => {
                console.log(navActive)
                setActiveIdx(idx);
                setNavActive(false);
              }}
              key={menu.text}
            >
              <NavItem active={activeIdx === idx} {...menu} />
            </div>
          ))}
        </div>
      </nav>
    </header>
  );
};

export default Navbar;