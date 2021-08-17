package com.programmers.realWorldSoftware;

import java.io.File;

public interface Importer {
    //다양한 종류의 문서임포트를 지원한다.
    //강한 형식으로 데이터의 형식을 파일의 확장자로 규제한다. -> 오류를 줄인다.
    Document importFile(File file) throws Exception;
}
