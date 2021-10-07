IDriveConnectionKit
===================

[![Build Status](https://travis-ci.org/hufman/IDriveConnectKit.svg?branch=master)](https://travis-ci.org/hufman/IDriveConnectKit)
[![Coverage Status](https://coveralls.io/repos/github/hufman/IDriveConnectKit/badge.svg?branch=master)](https://coveralls.io/github/hufman/IDriveConnectKit?branch=master)
[![Jitpack](https://jitpack.io/v/io.bimmergestalt/IDriveConnectKit.svg)](https://jitpack.io/#io.bimmergestalt/IDriveConnectKit)
![MIT Licensed](https://img.shields.io/github/license/hufman/IDriveConnectKit)

This is a collection of helper utilities for interacting with the BMW/Mini IDrive, including the IDrive Etch protocol definition and convenience classes for building RHMI applications.

IDrive Etch Protocol IDL
------------------------

Each phone app communicates to the IDrive with the Apache Etch RPC protocol. This library contains an IDL definition of this protocol,
and automatically builds the Etch proxy objects for ready consumption by the main application.

RHMI UI Description Parsing
---------------------------

Each IDrive RHMI application has a XML UI Description of the widget layout that is sent to the car.
This library provides parsing of this widget layout into high-level objects that can be interacted with,
in a disconnected fashion or connected to a live car. Each UI component is conveniently linked to any related models and action handlers. 

These widget objects can also be constructed manually, for testing purposes.

RHMI Event Dispatching
----------------------

After parsing the UI Description into high-level widgets, action and event handlers can be attached to each object,
ready for easy dispatching from the Etch callback receiver.

Examples
--------

Check out some of the [example applications](https://github.com/hufman/IDriveConnectKitDemos)!
