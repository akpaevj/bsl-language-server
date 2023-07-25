/*
 * This file is a part of BSL Language Server.
 *
 * Copyright (c) 2018-2023
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Fedkin <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package com.github._1c_syntax.bsl.languageserver.diagnostics;

import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticMetadata;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticScope;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticSeverity;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticTag;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticType;
import com.github._1c_syntax.bsl.languageserver.utils.bsl.Constructors;
import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.utils.CaseInsensitivePattern;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.regex.Pattern;

@DiagnosticMetadata(
  type = DiagnosticType.VULNERABILITY,
  severity = DiagnosticSeverity.MINOR,
  minutesToFix = 3,
  tags = {
    DiagnosticTag.SUSPICIOUS
  },
  scope = DiagnosticScope.BSL
)
public class FileSystemAccessDiagnostic extends AbstractFindMethodDiagnostic {
  private static final Pattern NEW_EXPRESSION = CaseInsensitivePattern.compile(
    "File|Файл|xBase|HTMLWriter|ЗаписьHTML|HTMLReader|ЧтениеHTML|FastInfosetReader|ЧтениеFastInfoset" +
      "|FastInfosetWriter|ЗаписьFastInfoset|XSLTransform|ПреобразованиеXSL" +
      "|ZipFileWriter|ЗаписьZipФайла|ZipFileReader|ЧтениеZipФайла|TextReader|ЧтениеТекста|TextWriter|ЗаписьТекста" +
      "|TextExtraction|ИзвлечениеТекста|BinaryData|ДвоичныеДанные|FileStream|ФайловыйПоток");

  private static final Pattern GLOBAL_METHODS = CaseInsensitivePattern.compile(
    "ЗначениеВФайл|ValueToFile|КопироватьФайл|FileCopy|ОбъединитьФайлы|MergeFiles|ПереместитьФайл|MoveFile" +
      "|РазделитьФайл|SplitFile|СоздатьКаталог|CreateDirectory|УдалитьФайлы|DeleteFiles" +
      "|КаталогПрограммы|BinDir|КаталогВременныхФайлов|TempFilesDir|КаталогДокументов|DocumentsDir" +
      "|РабочийКаталогДанныхПользователя|UserDataWorkDir" +
      "|НачатьПодключениеРасширенияРаботыСФайлами|BeginAttachingFileSystemExtension" +
      "|НачатьУстановкуРасширенияРаботыСФайлами|BeginInstallFileSystemExtension" +
      "|УстановитьРасширениеРаботыСФайлами|InstallFileSystemExtension" +
      "|УстановитьРасширениеРаботыСФайламиАсинх|InstallFileSystemExtensionAsync" +
      "|ПодключитьРасширениеРаботыСФайламиАсинх|AttachFileSystemExtensionAsync|" +
      "КаталогВременныхФайловАсинх|TempFilesDirAsync|КаталогДокументовАсинх|DocumentsDirAsync" +
      "|НачатьПолучениеКаталогаВременныхФайлов|BeginGettingTempFilesDir" +
      "|НачатьПолучениеКаталогаДокументов|BeginGettingDocumentsDir" +
      "|НачатьПолучениеРабочегоКаталогаДанныхПользователя|BeginGettingUserDataWorkDir" +
      "|РабочийКаталогДанныхПользователяАсинх|UserDataWorkDirAsync" +
      "|КопироватьФайлАсинх|CopyFileAsync|НайтиФайлыАсинх|FindFilesAsync|НачатьКопированиеФайла|BeginCopyingFile" +
      "|НачатьПеремещениеФайла|BeginMovingFile|НачатьПоискФайлов|BeginFindingFiles" +
      "|НачатьСозданиеДвоичныхДанныхИзФайла|BeginCreateBinaryDataFromFile" +
      "|НачатьСозданиеКаталога|BeginCreatingDirectory" +
      "|НачатьУдалениеФайлов|BeginDeletingFiles|ПереместитьФайлАсинх|MoveFileAsync" +
      "|СоздатьДвоичныеДанныеИзФайлаАсинх|CreateBinaryDataFromFileAsync|СоздатьКаталогАсинх|CreateDirectoryAsync" +
      "|УдалитьФайлыАсинх|DeleteFilesAsync");

  public FileSystemAccessDiagnostic() {
    super(GLOBAL_METHODS);
  }

  @Override
  protected boolean checkMethodCall(BSLParser.MethodCallContext ctx) {
    return false;
  }

  @Override
  public ParseTree visitNewExpression(BSLParser.NewExpressionContext ctx) {
    Constructors.typeName(ctx).ifPresent((String typeName) -> {
      var matcherTypeName = NEW_EXPRESSION.matcher(typeName);
      if (matcherTypeName.matches()) {
        diagnosticStorage.addDiagnostic(ctx);
      }
    });
    return super.visitNewExpression(ctx);
  }
}
