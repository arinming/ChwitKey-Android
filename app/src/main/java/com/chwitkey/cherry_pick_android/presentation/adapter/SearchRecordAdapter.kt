import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chwitkey.cherry_pick_android.data.model.SearchRecordEntity
import com.chwitkey.cherry_pick_android.databinding.ItemSearchBinding
import com.chwitkey.cherry_pick_android.presentation.ui.keyword.DeleteListener
import com.chwitkey.cherry_pick_android.presentation.ui.newsSearch.ArticleSearchFragment

class SearchRecordAdapter(
    private val deleteListener: DeleteListener,
    private var interactionListener: ArticleSearchFragment
) : RecyclerView.Adapter<SearchRecordAdapter.ViewHolder>() {
    private val records = ArrayList<SearchRecordEntity>()

    // 뷰 홀더 클래스
    inner class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: SearchRecordEntity) {
            binding.btnRecentSearchItem.text = record.record

            // 삭제 버튼 클릭 이벤트 설정
            binding.ibtnDeleteRecentSearch.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteListener.onDeleteClick(record.record)
                    // 삭제한 아이템만 제거하도록 수정
                    removeRecord(record)
                }
            }

            binding.btnRecentSearchItem.setOnClickListener {
                val searchText = binding.btnRecentSearchItem.text.toString()
                Log.d("검색어 클릭", searchText)
                interactionListener.onButtonSelected(searchText)
            }
        }
    }

    // 아이템 레이아웃 호출
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    // 호출한 내용으로 bind
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val record = records[position]
        viewHolder.bind(record)
    }

    // 데이터 크기 반환
    override fun getItemCount() = records.size

    fun setList(list: List<SearchRecordEntity>) {
        records.clear()
        records.addAll(list.reversed())
        notifyDataSetChanged()
    }

    fun getRecords(): List<SearchRecordEntity> {
        return records
    }


    fun removeRecord(record: SearchRecordEntity) {
        val position = records.indexOf(record)
        if (position != -1) {
            records.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}