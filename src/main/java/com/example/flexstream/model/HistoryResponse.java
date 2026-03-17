package com.example.flexstream.model;

public record HistoryResponse(int userId, int movieId, String title, float rating, long timestamp) {}
