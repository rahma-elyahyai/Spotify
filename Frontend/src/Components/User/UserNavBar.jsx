import React, { useEffect, useState } from "react";
import {
  Navbar,
  Nav,
  Form,
  FormControl,
  Container,
  Button,
  OverlayTrigger,
  Tooltip,
  Dropdown,
} from "react-bootstrap";
import "bootstrap-icons/font/bootstrap-icons.css";
import "/src/CSS/usernav.css";
import FilterAltIcon from '@mui/icons-material/FilterAlt';
import NotificationsIcon from '@mui/icons-material/Notifications';
import { Badge, IconButton } from "@mui/material";

import { Link } from "react-router-dom";
import BarChartIcon from '@mui/icons-material/BarChart';
import { NotificationsContext } from "../../Context/NotificationsContext";


const UserNavBar = ({
  name,
  onSearch,
  setSearchTerm,
  setSettings,
  settings,
  setArtistPage,
  setPlayListPage,
  setNotifications,
  setlyrics,
  setUserAnlayticsPage

}) => {

  const [showFilters, setShowFilters] = useState(false);

  const [search,setSearch]=useState("");
  

  const filterOptions = [
    { label: 'Genre', value: 'genre' },
    { label: 'Language', value: 'language' },
  ];

  const handleSearchChange = (e) => {
    setSearch(e.target.value);
    
  };

  useEffect(() => {
    console.log(search)
    onSearch(search);
    setSearchTerm(search);

  },[search])

  const handleSettingsClick = () => {
    setSettings(false);
    setSelectedFilters(null);
  };

  const handleApply = () => {
    setShowFilters(false);
    setFilter(true);
    console.log(selectedFilters);
    setsongs(false);


  }

  const handleHome = () => {
    console.log("home")
    setArtistPage(false);
    setPlayListPage(false);
    setlyrics(false);
    setSettings(true);
    setFilter(false);
    setSelectedFilters(null);
    setsongs(false);
    setUserAnlayticsPage(false);
  }

  const handleNotifications = () => {
    console.log("notifications")
    setNotifications(prev => !prev);
  }

  const handleAnalytics = () => {
    setUserAnlayticsPage(true);
    setPlayListPage(false);
    setArtistPage(false);
    setlyrics(false);
    setFilter(false);
    setSelectedFilters(null);
  }
  const { notification, selectedFilters, setSelectedFilters, setFilter, setsongs } = React.useContext(NotificationsContext);


  return (
    <Navbar
      variant="dark"
      expand="lg"
      fixed="top"
      style={{ backgroundColor: "#111" }}
    >
      <Container>
        <Navbar.Brand onClick={() => handleHome()}>
          <i className="bi bi-house-door-fill" style={{ fontSize: "24px", cursor: "pointer" }}></i>
        </Navbar.Brand>
        <Nav className="me-auto">
          <Nav.Link onClick={() => handleHome()}>Home</Nav.Link>
        </Nav>


        {settings ? (
          <>
            <div
              style={{
                color: "white",
                display: "flex",
                alignContent: "center",
                alignItems: "center",

              }}
            >
              <h3>Welcome, {name}!</h3>
            </div>



            <Form inline className="d-flex mx-auto searchContainer">
              <FormControl
                type="text"
                placeholder="Search for songs... "
                className="me-sm-2"
                style={{ width: "400px" }}
                onChange={handleSearchChange}
                value={search}
              />

              <Dropdown
                align="end"
                onToggle={(isOpen) => setShowFilters(isOpen)}
                show={showFilters}
              >
                <Dropdown.Toggle
                  as={IconButton}
                  sx={{
                    color: "white",
                    height: "35px",
                    marginLeft: "8px"
                  }}
                >
                  <OverlayTrigger
                    placement="bottom"
                    overlay={<Tooltip id="tooltip-bottom">Filter</Tooltip>}
                  >

                    <FilterAltIcon />
                  </OverlayTrigger>
                </Dropdown.Toggle>

                <Dropdown.Menu
                  style={{
                    position: 'absolute',
                    right: 0,
                    left: 'auto',
                    minWidth: '200px',
                    backgroundColor: '#222',
                    border: '1px solid #333'
                  }}
                >
                  <div className="px-3 py-2">
                    <h6 className="text-white mb-3">Filter By</h6>

                    {filterOptions.map((option) => (
                      <Form.Check
                        key={option.value}
                        type="radio"
                        name="filterOptions"
                        label={option.label}
                        checked={selectedFilters === option.value}
                        onChange={() => setSelectedFilters(option.value)}
                        className="text-white mb-2"
                      />
                    ))}

                    <div className="d-flex justify-content-between mt-3">
                      <Button
                        variant="outline-secondary"
                        size="sm"
                        onClick={() => setSelectedFilters(null)}
                      >
                        Clear
                      </Button>
                      <Button
                        variant="primary"
                        size="sm"
                        onClick={() => handleApply()}
                      >
                        Apply
                      </Button>
                    </div>
                  </div>
                </Dropdown.Menu>
              </Dropdown>
            </Form>

            <OverlayTrigger
              placement="bottom"
              overlay={<Tooltip id="tooltip-bottom">Analytics</Tooltip>}
            >


              <IconButton sx={{ color: "white", height: "30px", marginRight: "30px" }} onClick={() => handleAnalytics()}>

                <BarChartIcon />

              </IconButton>

            </OverlayTrigger>
          </>
        ) : (
          ""
        )}


        <Nav className="ml-auto">
          <OverlayTrigger
            placement="bottom"
            overlay={<Tooltip id="tooltip-bottom">Settings</Tooltip>}
          >
            <Nav.Link onClick={() => handleSettingsClick()}>
              <Button variant="outline-success">
                <i className="bi bi-gear-fill" style={{ fontSize: "20px" }} />
              </Button>
            </Nav.Link>
          </OverlayTrigger>
          <OverlayTrigger
            placement="bottom"
            overlay={<Tooltip id="tooltip-bottom">Log out</Tooltip>}
          >
            <Navbar.Brand as={Link} to="/" className="ml-auto d-flex align-items-center">
              <Button onclick={() => localStorage.clear()} variant="outline-danger" style={{ fontSize: "20px" }}>
                <i className="bi bi-box-arrow-right" />
              </Button>
            </Navbar.Brand>
          </OverlayTrigger>

        </Nav>


        {
          notification > 0 ? (
            <OverlayTrigger
              placement="bottom"
              overlay={<Tooltip id="tooltip-bottom">Notifications</Tooltip>}
            >
              <Badge badgeContent={notification} color="success">

                <IconButton sx={{ color: "white", height: "30px" }} onClick={() => handleNotifications()}>
                  <NotificationsIcon />
                </IconButton>
              </Badge>
            </OverlayTrigger>
          ) : (
            <OverlayTrigger
              placement="bottom"
              overlay={<Tooltip id="tooltip-bottom">Notifications</Tooltip>}
            >
              <IconButton sx={{ color: "white", height: "30px" }} onClick={() => handleNotifications()}>
                <NotificationsIcon />
              </IconButton>
            </OverlayTrigger>
          )
        }

      </Container>
    </Navbar>
  );
};

export default UserNavBar;
