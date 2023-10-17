package per.khalilov.crudproject.repositories.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import per.khalilov.crudproject.exceptions.FileRepositoryException;
import per.khalilov.crudproject.exceptions.HolderFileException;
import per.khalilov.crudproject.mappers.ProductMapper;
import per.khalilov.crudproject.models.Product;
import per.khalilov.crudproject.repositories.ProductRepository;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@ConditionalOnProperty(value = "project.repository.fileImplementation.enabled")
@Repository
public class ProductRepositoryFileImpl implements ProductRepository {
    private final ProductMapper productMapper;
    @Value("${project.holderFileDirectory}")
    private String holderFileDir;
    @Value("${project.holderFileName}")
    private String holderFileName;

    private Path getHolderPath() {
        Path parents = Paths.get(holderFileDir);
        Path file = Paths.get(holderFileDir + "/" + holderFileName);
        try {
            if (!Files.exists(parents)) {
                Files.createDirectories(parents);
            } if (!Files.exists(file)) {
                Files.createFile(file);
            }
        } catch (IOException e) {
            throw new HolderFileException("Holder file error...");
        }
        return file;
    }

    @Override
    public synchronized Product save(Product product) {
        Path path = getHolderPath();
        try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(path,
                StandardOpenOption.APPEND,
                StandardOpenOption.WRITE))) {
            writer.writeNext(productMapper.toStringArray(product));
        } catch (IOException e) {
            throw new FileRepositoryException("Save process error...");
        }
        return product;
    }

    @Override
    public synchronized Product update(Product newProduct) {
        Path path = getHolderPath();
        String[] newProductStringArray = productMapper.toStringArray(newProduct);
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(path));
             CSVWriter writer = new CSVWriter(Files.newBufferedWriter(path, StandardOpenOption.WRITE))) {
            List<String[]> result = reader.readAll().stream()
                    .map(line ->
                            Objects.equals(newProductStringArray[0], line[0]) ? newProductStringArray : line
                    ).toList();
            writer.writeAll(result);
        } catch (CsvException | IOException e) {
            throw new FileRepositoryException("Update process error...");
        }
        return newProduct;
    }

    @Override
    public synchronized void deleteByVendorCode(String vendorCode) {
        Path path = getHolderPath();
        CSVWriter writer = null;
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(path))) {
            List<String[]> result = reader.readAll().stream()
                    .filter(line -> !vendorCode.equals(line[0]))
                    .toList();
            // К сожалению, не получилось сделать как в update - файл ломался, т.к. последняя строка не удалялась,
            // в итоге получалось, что 2 сущности с одним vendorCode
            writer = new CSVWriter(Files.newBufferedWriter(path,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING));
            writer.writeAll(result);
        } catch (CsvException | IOException e) {
            throw new FileRepositoryException("Delete process error...");
        } finally {
            closeStream(writer);
        }
    }

    @Override
    public synchronized List<Product> findAll() {
        try(CSVReader reader = new CSVReader(Files.newBufferedReader(getHolderPath()))) {
            return reader.readAll().stream()
                    .map(productMapper::fromStringArray)
                    .toList();
        } catch (CsvException | IOException e) {
            throw new FileRepositoryException("Read process error...");
        }
    }

    @Override
    public synchronized Optional<Product> findByVendorCode(String vendorCode) {
        try(CSVReader reader = new CSVReader(Files.newBufferedReader(getHolderPath()))) {
            return reader.readAll().stream()
                    .filter((line) -> vendorCode.equals(line[0]))
                    .map(productMapper::fromStringArray)
                    .findFirst();
        } catch (CsvException | IOException e) {
            throw new FileRepositoryException("Read process error...");
        }
    }

    private void closeStream(Closeable closeable) {
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                throw new FileRepositoryException("Close io-stream error...");
            }
        }
    }
}
