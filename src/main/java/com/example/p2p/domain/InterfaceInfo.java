package com.example.p2p.domain;

import lombok.Data;

import java.util.List;

@Data
public class InterfaceInfo {
    private int total;
    private List<ChainList> list;
}
