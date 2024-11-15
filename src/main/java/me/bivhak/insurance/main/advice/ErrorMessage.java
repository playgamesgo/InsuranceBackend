package me.bivhak.insurance.main.advice;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message, String description) {

}