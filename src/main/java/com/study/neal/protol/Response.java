package com.study.neal.protol;

import lombok.*;

/**
 * @author yedunyao
 * @since 2020/12/18 15:49
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response extends Packet {

    private long id;

    private Object result;

}
