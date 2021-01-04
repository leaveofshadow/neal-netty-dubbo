package com.study.neal.protol;

import lombok.*;

/**
 * @author yedunyao
 * @since 2020/12/18 15:47
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request extends Packet {

    private long id;

    private String className;

    private String methodName;

    private Object[] params;

}
