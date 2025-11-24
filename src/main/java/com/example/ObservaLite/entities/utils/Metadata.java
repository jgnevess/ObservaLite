package com.example.ObservaLite.entities.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
	private String ip;
    private String userAgent;
    private Long durationMs;
    private String extraInfo;
}
