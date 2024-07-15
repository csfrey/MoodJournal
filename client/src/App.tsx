import { Button } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useCookies } from "react-cookie";

import logo from "./logo.svg";

import "./App.css";

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(false);
  const [user, setUser] = useState(undefined);
  const [cookies] = useCookies(["XSRF-TOKEN"]);

  useEffect(() => {
    setLoading(true);
    fetch("api/user", { credentials: "include" })
      .then((response) => response.text())
      .then((body) => {
        console.log(body)
        if (body === "") {
          setAuthenticated(false);
        } else {
          setUser(JSON.parse(body));
          setAuthenticated(true);
        }
        setLoading(false);
      });
  }, [setAuthenticated, setLoading, setUser]);

  const login = () => {
    let port = window.location.port ? ":" + window.location.port : "";
    if (port === ":3000") {
      port = ":8080";
    }
    // redirect to a protected URL to trigger authentication
    window.location.href = `//${window.location.hostname}${port}/api/private`;
  };

  const logout = () => {
    fetch("/api/logout", {
      method: "POST",
      credentials: "include",
      headers: { "X-XSRF-TOKEN": cookies["XSRF-TOKEN"] },
    })
      .then((res) => res.json())
      .then((response) => {
        window.location.href =
          `${response.logoutUrl}?id_token_hint=${response.idToken}` +
          `&post_logout_redirect_uri=${window.location.origin}`;
      });
  };

  const button = authenticated ? (
    <div>
      <Button onClick={logout}>Logout</Button>
    </div>
  ) : (
    <div>
      <Button onClick={login}>Login</Button>
    </div>
  );

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <div>{loading ? "Loading..." : button}</div>
      </header>
    </div>
  );
}

export default App;
