package org.hackerpeers.sqli.analyzer;

import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
public class SQLInjectionRepositoryFixedInMemoryTest extends ISQLInjectionRepositoryTest<SQLInjectionRepositoryFixedInMemory> {
    private static final String A_THIRD_ENTRY_POINT = "a.third.entry.point";

    @Override
    SQLInjectionRepositoryFixedInMemory createRepo() {
        return new SQLInjectionRepositoryFixedInMemory(cfg, A_BUFFER_SIZE);
    }

    @Test
    public void whenRepoIsCreatedItIsEmpty() throws IOException {
        assertThat(repo.getEntries().size(), equalTo(0));
    }

    @Test
    public void whenRepoIsFullAddingANewEntryRemovesTheOldest() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, ONE_VARIATION);
        repo.addStatement(ANOTHER_ENTRY_POINT, A_SIMPLE_SQL, ONE_VARIATION);
        assertThat(repo.getEntries().size(), is(A_BUFFER_SIZE)); //Tests messed-up

        repo.addStatement(A_THIRD_ENTRY_POINT, A_SIMPLE_SQL, ONE_VARIATION);

        assertThat(repo.getEntries().size(), is(2));
        //Oldest item removed
        assertThat(repo.getEntries(), not(hasItem(new SQLInjectionAnalyzerEntryMatcher(AN_ENTRY_POINT, A_SIMPLE_SQL, 1, ONE_VARIATION))));
        //newest item in there
        assertThat(repo.getEntries(), hasItem(new SQLInjectionAnalyzerEntryMatcher(A_THIRD_ENTRY_POINT, A_SIMPLE_SQL, 1, ONE_VARIATION)));
    }
}